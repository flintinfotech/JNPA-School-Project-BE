package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.FeePayment;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SchoolExpensesDTO {

    private Long schoolExpenseId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;
    private Long purchaseId;
    private FeePayment status;
    private PurchaseDTO purchaseDTO;

}
