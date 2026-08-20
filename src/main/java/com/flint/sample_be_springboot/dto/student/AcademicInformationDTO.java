package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicInformationDTO {

    private Long academicInformationId;
    private Long admissionNo;
    private Long studentId;
    private LocalDate admissionDate;
    private String standard;
    private String division;
    private String rollNo;
    private String academicYear;
    private AuditDetails auditDetails;

}
