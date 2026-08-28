package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.enums.FeePayment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeePaymentDTO {

    private Long feePaymentId;
    private Long studentFeeId;
    private BigDecimal amount;
    private String paymentMode;
    private LocalDate paymentDate;
    private String transactionId;
    private String receiptNo;
    private String remarks;
    private FeePayment status;

}
