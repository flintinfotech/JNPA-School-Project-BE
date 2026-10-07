package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.FeePayment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SchoolExpensesDTO {

    private Long schoolExpenseId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private String academicYear;
    private LocalDate purchaseDate;
    private FeePayment status;

    private String productCode;
    private String category;
    private String productName;

    private String document;

    private Long vendorMasterId;
    private VendorMasterDTO vendorMasterDTO;

    private Long purchaseId;
    private PurchaseDTO purchaseDTO;
}
