package com.flint.sample_be_springboot.dto.admission;

import lombok.Data;

@Data
public class RequiredDocumentDTO {

    private Long requiredDocumentId;
    private Long admissionId;
    private String documentName;

}
