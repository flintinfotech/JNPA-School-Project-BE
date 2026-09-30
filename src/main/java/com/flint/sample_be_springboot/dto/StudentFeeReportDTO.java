package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class StudentFeeReportDTO {

    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private List<StudentFeeReportDataDTO> feeReportDataDTOS;
    private LocalDate printDate = LocalDate.now();
    private String printTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
}
