package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Table(name = "STUDENT_FEE_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentFeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_FEE_ID")
    private Long studentFeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private StudentEntity studentEntity;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Column(name = "FEE_NAME")
    private String feeName;

    @Column(name = "TOTAL_FEE_AMOUNT")
    private BigDecimal totalFeeAmount;

    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;

    @Column(name = "PENDING_AMOUNT")
    private BigDecimal pendingAmount;

    @Column(name = "DUE_AMOUNT")
    private BigDecimal dueAmount;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentFee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeePaymentEntity> feePaymentEntities;

    @Embedded
    private AuditDetails auditDetails;

}
