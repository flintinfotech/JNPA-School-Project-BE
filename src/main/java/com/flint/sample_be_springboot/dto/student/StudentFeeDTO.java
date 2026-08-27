package com.flint.sample_be_springboot.dto.student;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentFeeDTO {

    private Long studentFeeId;
    private Long studentId;
    private String academicYear;
    private String feeName;
    private BigDecimal totalFeeAmount;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private BigDecimal dueAmount;
    // need to add due date
    private LocalDate dueDate;



    private List<FeePaymentDTO> feePaymentDTOS;

}
