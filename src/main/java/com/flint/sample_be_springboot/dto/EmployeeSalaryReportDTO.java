package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.Role;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
public class EmployeeSalaryReportDTO {

    private String firstName;
    private String lastName;
    private Role role;
    private String designation;
    private String department;

    private BigDecimal totalBasicSalary;
    private BigDecimal totalHra;
    private BigDecimal totalTransportAllowance;
    private BigDecimal totalMedicalAllowance;
    private BigDecimal totalOtherAllowance;
    private BigDecimal totalDeduction;
    private BigDecimal totalNetSalary;
    //    private List<EmployeeSalaryReportDataDTO> reportDataDTOList;
    private LocalDate printDate = LocalDate.now();
    private String printTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));

}
