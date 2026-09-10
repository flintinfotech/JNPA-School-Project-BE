package com.flint.sample_be_springboot.entity.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.FormerStudentDocumentStatus;
import com.flint.sample_be_springboot.enums.FormerStudentDocumentType;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "DOCUMENT_TYPE", nullable = false)
    private FormerStudentDocumentType documentType;

    // Mainly required for MARKSHEET.
    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    // Mainly required for MARKSHEET
    @Column(name = "STANDARD")
    private String standard;

    // Date on which the document was issued/generated
    @Column(name = "DOCUMENT_DATE")
    private LocalDate documentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FormerStudentDocumentStatus documentStatus;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Column(name = "UPLOAD_DATE")
    private LocalDate uploadDate;

    @Column(name = "COLLECTED_DATE")
    private LocalDate collectedDate;

    @Column(name = "COLLECTED_BY")
    private String collectedBy;

    // Example: STUDENT, FATHER, MOTHER, GUARDIAN
    @Column(name = "COLLECTED_RELATION")
    private String collectedRelation;

    @Column(name = "REMARK")
    private String remark;

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

