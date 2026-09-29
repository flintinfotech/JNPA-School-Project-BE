package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "EMPLOYEE_SALARY_ENTITY")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeSalaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_SALARY_ID")
    @EqualsAndHashCode.Include
    private Long employeeSalaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_DETAILS_ID")
    private EmployeeDetailsEntity employeeDetailsEntity;

    @Column(name = "BASIC_SALARY")
    private BigDecimal basicSalary;

    @Column(name = "HRA")
    private BigDecimal hra;

    @Column(name = "TRANSPORT_ALLOWANCE")
    private BigDecimal transportAllowance;

    @Column(name = "MEDICAL_ALLOWANCE")
    private BigDecimal medicalAllowance;

    @Column(name = "OTHER_ALLOWANCE")
    private BigDecimal otherAllowance;

    @Column(name = "DEDUCTION")
    private BigDecimal deduction;

    @Column(name = "NET_SALARY")
    private BigDecimal netSalary;

    @Column(name = "REMARK")
    private String remark;

}
