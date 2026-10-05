package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "REQUEST_APPROVAL_ENTITY")
@AllArgsConstructor
@NoArgsConstructor
public class RequestApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "REQUEST_APPROVAL_ID")
    private Long requestApprovalId;

    @Column(name = "REQUEST_TYPE")
    private String requestType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "REQUEST_DATE")
    private LocalDate requestedDate;

    @Column(name = "ESTIMATED_AMOUNT")
    private BigDecimal estimatedAmount;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "QUANTITY")
    private String Quantity;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Embedded
    private AuditDetails auditDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ID")
    private PurchaseEntity purchaseEntity;

}
