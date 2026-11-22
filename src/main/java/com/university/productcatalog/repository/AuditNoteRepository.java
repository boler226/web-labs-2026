package com.university.productcatalog.repository;

import com.university.productcatalog.entity.AuditNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditNoteRepository extends JpaRepository<AuditNote, Long> { }
