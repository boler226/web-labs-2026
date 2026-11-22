package com.university.productcatalog.controller;

import com.university.productcatalog.entity.AuditNote;
import com.university.productcatalog.repository.AuditNoteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/audit-notes")
public class AuditNoteController {
    private final AuditNoteRepository notes;

    public AuditNoteController(AuditNoteRepository notes) {
        this.notes = notes;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public List<NoteResponse> list() {
        return notes.findAll().stream().map(NoteResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    public NoteResponse get(@PathVariable Long id) {
        return NoteResponse.from(notes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Audit note not found")));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request,
                                                Authentication authentication,
                                                UriComponentsBuilder uriBuilder) {
        AuditNote note = new AuditNote();
        note.setText(request.text());
        note.setAuthor(authentication.getName());
        note.setCreatedAt(Instant.now());
        AuditNote saved = notes.save(note);
        var location = uriBuilder.path("/api/audit-notes/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(NoteResponse.from(saved));
    }

    public record NoteRequest(@NotBlank @Size(max = 1000) String text) { }
    public record NoteResponse(Long id, String text, String author, Instant createdAt) {
        static NoteResponse from(AuditNote note) {
            return new NoteResponse(note.getId(), note.getText(), note.getAuthor(), note.getCreatedAt());
        }
    }
}
