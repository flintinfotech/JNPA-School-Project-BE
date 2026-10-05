package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class StudentFeeReportDataDTO {
    private String academicYear;
//    private String feeName;
    private BigDecimal totalFeeAmount;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
}
