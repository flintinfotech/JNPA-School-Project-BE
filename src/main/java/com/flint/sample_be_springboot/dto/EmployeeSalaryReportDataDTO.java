package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeSalaryReportDataDTO {

    private LocalDate salaryDate;
    private BigDecimal basicSalary;
    private BigDecimal hra;
    private BigDecimal transportAllowance;
    private BigDecimal medicalAllowance;
    private BigDecimal otherAllowance;
    private BigDecimal deduction;
    private BigDecimal netSalary;

}
