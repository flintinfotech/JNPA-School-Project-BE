package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.student.StudentFeeEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeePaymentDTO {

    private Long paymentId;
    private StudentFeeEntity studentFee;
    private BigDecimal amount;
    private String paymentMode;
    private LocalDate paymentDate;
    private String transactionId;
    private String remarks;

}
