package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class SchoolExpensesReportDTO {

    private  String productCode;
    private String category;
    private String productName;
    private List<SchoolExpensesEntity> schoolExpensesEntities;
    private LocalDate printDate = LocalDate.now();
    private String printTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
}
