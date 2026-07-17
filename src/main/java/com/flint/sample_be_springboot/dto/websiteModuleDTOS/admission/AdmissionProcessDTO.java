package com.flint.sample_be_springboot.dto.websiteModuleDTOS.admission;

import lombok.Data;

@Data
public class AdmissionProcessDTO {

    private Long admissionProcessId;
    private Long admissionId;
    private String stepNo;
    private String heading;
    private String description;

}
