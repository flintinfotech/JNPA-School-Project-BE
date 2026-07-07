package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubScreenDTO {

    private Long subScreenId;
    private Long academicYearId;
    private String subScreenName;
    private List<SubScreenDataDTO> subScreenDataEntities;

}
