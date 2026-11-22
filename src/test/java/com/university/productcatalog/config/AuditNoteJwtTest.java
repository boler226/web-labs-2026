package com.university.productcatalog.config;

import com.university.productcatalog.controller.AuditNoteController;
import com.university.productcatalog.entity.AuditNote;
import com.university.productcatalog.repository.AuditNoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditNoteController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class AuditNoteJwtTest {
    @Autowired MockMvc mvc;
    @Autowired JwtEncoder encoder;
    @MockBean AuditNoteRepository notes;

    @Test
    void jwtRolesControlAuditNoteAccess() throws Exception {
        when(notes.findAll()).thenReturn(List.of());
        when(notes.save(any(AuditNote.class))).thenAnswer(invocation -> {
            AuditNote note = invocation.getArgument(0);
            var id = AuditNote.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(note, 1L);
            return note;
        });

        mvc.perform(get("/api/audit-notes").header("Authorization", bearer("viewer", "VIEWER")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/audit-notes").header("Authorization", bearer("editor", "EDITOR")))
                .andExpect(status().isOk());
        mvc.perform(post("/api/audit-notes").header("Authorization", bearer("editor", "EDITOR"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Lab note\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/audit-notes").header("Authorization", bearer("admin", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Lab note\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value("admin"));
    }

    private String bearer(String user, String role) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder().issuer("product-catalog").subject(user)
                .issuedAt(now).expiresAt(now.plusSeconds(3600)).claim("roles", List.of(role)).build();
        return "Bearer " + encoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }
}
