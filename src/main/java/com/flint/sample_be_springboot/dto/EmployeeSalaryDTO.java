package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeSalaryDTO {

    private BigDecimal basicSalary;
    private BigDecimal hra;
    private BigDecimal transportAllowance;
    private BigDecimal medicalAllowance;
    private BigDecimal otherAllowance;
    private BigDecimal deduction;
    private BigDecimal netSalary;
    private String remark;
}
