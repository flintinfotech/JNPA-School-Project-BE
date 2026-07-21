package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDocumentDTO {

    private Long userDocumentId;
    private Long userInformationId;
    private String documentName;
    private String documentType;
    private LocalDate uploadDate;
    private String document;

}
