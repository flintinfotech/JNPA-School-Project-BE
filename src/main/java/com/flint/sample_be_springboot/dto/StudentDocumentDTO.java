package com.flint.sample_be_springboot.dto;

import lombok.Data;

@Data
public class StudentDocumentDTO {

    private Long studentDocumentId;
    private Long studentId;
    private String birthCertificate;
    private String transferCertificate;
    private String aadhaarCard;
    private String photo;
    private String medicalCertificate;

}
