package com.university.productcatalog.config;

import com.university.productcatalog.controller.ProductRestController;
import com.university.productcatalog.entity.Product;
import com.university.productcatalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    private static final String PRODUCT_JSON = """
            {"name":"Lab keyboard","category":"Electronics","price":79.99,"quantity":25}
            """;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    void missingAndInvalidCredentialsAreRejected() throws Exception {
        mvc.perform(get("/api/products")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/products").header("Authorization", basic("admin", "wrong")))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(productService);
    }

    @Test
    void viewerCanReadButCannotCreate() throws Exception {
        when(productService.findAll(null)).thenReturn(List.of());

        mvc.perform(get("/api/products").header("Authorization", basic("viewer", "viewer123")))
                .andExpect(status().isOk());
        mvc.perform(post("/api/products").header("Authorization", basic("viewer", "viewer123"))
                        .contentType(MediaType.APPLICATION_JSON).content(PRODUCT_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editorCanCreateAndUpdateButCannotDelete() throws Exception {
        when(productService.create(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(42L);
            return product;
        });
        when(productService.update(eq(42L), any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(1);
            product.setId(42L);
            return product;
        });

        mvc.perform(post("/api/products").header("Authorization", basic("editor", "editor123"))
                        .contentType(MediaType.APPLICATION_JSON).content(PRODUCT_JSON))
                .andExpect(status().isCreated());
        mvc.perform(put("/api/products/42").header("Authorization", basic("editor", "editor123"))
                        .contentType(MediaType.APPLICATION_JSON).content(PRODUCT_JSON))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/products/42").header("Authorization", basic("editor", "editor123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDelete() throws Exception {
        mvc.perform(delete("/api/products/42").header("Authorization", basic("admin", "admin123")))
                .andExpect(status().isNoContent());
    }

    private static String basic(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
