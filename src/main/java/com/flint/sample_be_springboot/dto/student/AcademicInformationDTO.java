package com.flint.sample_be_springboot.dto.student;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicInformationDTO {

    private Long academicInformationId;
    private Long admissionNo;
    private Long studentId;
    private LocalDate admissionDate;
    private String standard;
    private String section;
    private String rollNo;
    private String academicYear;

}
