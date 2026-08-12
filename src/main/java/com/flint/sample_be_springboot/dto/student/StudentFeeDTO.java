package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.entity.student.FeePaymentEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StudentFeeDTO {

    private Long studentFeeId;
    private StudentEntity studentEntity;
    private String academicYear;
    private String feeName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private BigDecimal dueAmount;
    private List<FeePaymentEntity> feePaymentEntities;


}
