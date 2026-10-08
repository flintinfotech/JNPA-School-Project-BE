package com.flint.sample_be_springboot.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RequestApprovalDTO {

    private Long requestApprovalId;
    private String requestType;
    private String remark;
    private LocalDate requestedDate;
    private BigDecimal estimatedAmount;
    private String priority;
    private String Quantity;
    private String status;
    private String academicYear;
    private PurchaseDTO purchaseDTO;
    private String cancellationReason;
    private String orderNumber;

}
