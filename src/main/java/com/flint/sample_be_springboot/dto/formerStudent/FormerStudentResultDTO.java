package com.flint.sample_be_springboot.dto.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FormerStudentResultDTO {

    private Long formerResultId;
    private Long formerStudentId;
    private String standard;
    private String division;
    private String examType;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<FormerExamSubjectsDTO> formerExamSubjectsDTOS;
    private BigDecimal totalMarks;
    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String grade;
    private String resultStatus;
    private AuditDetails auditDetails;

}