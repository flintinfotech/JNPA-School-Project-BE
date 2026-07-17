package com.flint.sample_be_springboot.dto.websiteModuleDTOS.admission;

import lombok.Data;

import java.util.List;

@Data
public class AdmissionDTO {

    private Long admissionId;
    private String classRoomName;
    private String academicYearName;
    private String medium;
    private String brochure;
    List<EligibilityCriteriaDTO> eligibilityCriteriaDTOS;
    List<ImportantDateDTO> importantDateDTOS;
    List<RequiredDocumentDTO> requiredDocumentDTOS;
    List<AdmissionProcessDTO> admissionProcessDTOS;

}
