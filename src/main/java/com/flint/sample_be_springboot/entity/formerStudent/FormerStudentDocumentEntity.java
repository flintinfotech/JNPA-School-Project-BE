package com.flint.sample_be_springboot.entity.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Base64;

@Table(name = "FORMER_STUDENT_DOCUMENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormerStudentDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "FORMER_STUDENT_DOCUMENT_ID")
    private Long formerStudentDocumentId;

    @ManyToOne(targetEntity = FormerStudentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORMER_STUDENT_ID")
    private FormerStudentEntity formerStudentEntity;

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

