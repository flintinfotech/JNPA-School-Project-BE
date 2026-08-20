package com.flint.sample_be_springboot.dto.student;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentResultDTO {

    private Long resultId;
    private Long studentId;
    private String standard;
    private String division;
    private String examType;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ExamSubjectsDTO> examSubjectsDTOS;
    private BigDecimal totalMarks;
    private BigDecimal obtainedMarks;
    private BigDecimal percentage;
    private String grade;
    private String resultStatus;
}