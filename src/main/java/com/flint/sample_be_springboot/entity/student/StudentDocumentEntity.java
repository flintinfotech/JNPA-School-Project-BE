package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Base64;

@Table(name = "STUDENT_DOCUMENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "STUDENT_DOCUMENT_ID")
    private Long studentDocumentId;

    @ManyToOne(targetEntity = StudentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @NonNull
    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "UPLOAD_DATE")
    private LocalDate uploadDate;

    @Lob
    @Column(name = "DOCUMENT")
    private byte[] document;

    @Embedded
    private AuditDetails auditDetails;

    public String getDocument() {
        if (document != null) {
            return Base64.getEncoder().encodeToString(document);
        }
        return null;
    }

}
