package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "BIRTH_CERTIFICATE")
    private byte[] birthCertificate;

    @Column(name = "TRANSFER_CERTIFICATE")
    private byte[] transferCertificate;

    @Column(name = "AADHAAR_CARD")
    private byte[] aadhaarCard;

    @Column(name = "PHOTO")
    private byte[] photo;

    @Column(name = "MEDICAL_CERTIFICATE")
    private byte[] medicalCertificate;

}
