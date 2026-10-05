package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class SchoolExpensesReportDTO {

    private  String productCode;
    private String category;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private BigDecimal total;
    private LocalDate purchaseDate;
    private String academicYear;
    private LocalDate printDate = LocalDate.now();
    private String printTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
}
