package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;

import com.flint.sample_be_springboot.enums.FeePayment;
import com.flint.sample_be_springboot.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name = "FEE_PAYMENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEE_PAYMENT_ID")
    private Long feePaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_FEE_ID", nullable = false)
    private StudentFeeEntity studentFee;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "PAYMENT_MODE")
    private String paymentMode;

    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate;

    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Column(name = "RECEIPT_NO")
    private String receiptNo;

    @Column(name = "REMARKS")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FeePayment status;

    @Embedded
    private AuditDetails auditDetails;

}
