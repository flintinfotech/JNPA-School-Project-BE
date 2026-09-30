package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SchoolExpensesReportDataDTO {

    private BigDecimal price;
    private Integer quantity;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private BigDecimal total;
    private LocalDate purchaseDate;
    private String academicYear;
}
