package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "VENDOR_MASTER_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "VENDOR_MASTER_ID")
    private Long vendorMasterId;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PIN_CODE")
    private String pinCode;

    @Column(name = "PAN_NUMBER")
    private String panNumber;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "VENDOR_TYPE")
    private String vendorType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Embedded
    private AuditDetails auditDetails;

    @OneToMany(mappedBy = "vendorMasterEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchoolExpensesEntity> schoolExpensesEntities;


}
