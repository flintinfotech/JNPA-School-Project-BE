package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicYearDTO {

    private Long academicYearId;
    private String academicYearName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;

}
