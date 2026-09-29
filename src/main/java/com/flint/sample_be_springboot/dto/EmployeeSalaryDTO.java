package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeSalaryDTO {

    private BigDecimal basicSalary;
    private Long employeeDetailsId;
    private LocalDate salarDate;
    private BigDecimal hra;
    private BigDecimal transportAllowance;
    private BigDecimal medicalAllowance;
    private BigDecimal otherAllowance;
    private BigDecimal deduction;
    private BigDecimal netSalary;
    private String remark;
    private String academicYear;

}
