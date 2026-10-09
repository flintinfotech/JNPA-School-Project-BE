package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.FeePayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "SCHOOL_EXPENSES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHOOL_EXPENSE_ID")
    private Long schoolExpenseId;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;

    @Column(name = "PENDING_AMOUNT")
    private BigDecimal pendingAmount;

    @Column(name = "TOTAL")
    private BigDecimal total;

    @Column(name = "PURCHASE_DATE")
    private LocalDate purchaseDate;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FeePayment status;

    @Column(name = "PRODUCT_CODE", nullable = false)
    private  String productCode;

    @Column(name = "CATEGORY", nullable = false)
    private String category;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;


    @Column(name = "ORDER_NUMBER")
    private String orderNumber;

    @Lob
    @Column(name = "DOCUMENT")
    private byte[] document;

    @Embedded
    private AuditDetails auditDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_MASTER_ID")
    private VendorMasterEntity vendorMasterEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ID")
    private PurchaseEntity purchaseEntity;

    public String getDocument() {
        if (document != null) {
            return Base64.getEncoder().encodeToString(document);
        }
        return null;
    }

}

