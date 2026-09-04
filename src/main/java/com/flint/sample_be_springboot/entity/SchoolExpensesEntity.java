package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.FeePayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(name = "TOTAL")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FeePayment status;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "PURCHASE_ID")
    private PurchaseEntity purchaseEntity;

}

