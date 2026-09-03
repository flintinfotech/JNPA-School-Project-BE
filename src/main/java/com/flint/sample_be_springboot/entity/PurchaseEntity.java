package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PURCHASE_ENTITY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_ID")
    private Long purchaseId;

    @Column(name = "PRODUCT_CODE", nullable = false)
    private  String productCode;

    @Column(name = "CATEGORY", nullable = false)
    private String category;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;


}
