package com.university.productcatalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.Instant;

@Entity
@Table(name = "audit_notes")
public class AuditNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Instant createdAt;

    public Long getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
