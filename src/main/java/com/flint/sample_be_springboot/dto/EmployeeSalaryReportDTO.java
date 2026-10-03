package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.Role;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class EmployeeSalaryReportDTO {

    private String firstName;
    private String lastName;
    private Role role;
    private String designation;
    private String department;
    private List<EmployeeSalaryReportDataDTO> reportDataDTOList;
    private LocalDate printDate = LocalDate.now();
    private String printTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));

}
