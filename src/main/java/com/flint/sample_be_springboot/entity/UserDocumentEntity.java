package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "USER_DOCUMENT_ENTITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "USER_DOCUMENT_ID")
    private Long userDocumentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_INFORMATION_ID")
    private UserInformationEntity userInformationEntity;

    @NonNull
    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "DOCUMENT_TYPE")
    private String documentType;

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