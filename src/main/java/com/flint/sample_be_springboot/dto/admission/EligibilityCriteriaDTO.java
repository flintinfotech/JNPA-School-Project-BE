package com.flint.sample_be_springboot.dto.admission;

import lombok.Data;

@Data
public class EligibilityCriteriaDTO {

    private Long eligibilityCriteriaId;
    private Long admissionId;
    private String title;
    private String description;

}
