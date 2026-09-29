package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLOYEE_SALARY_ENTITY")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class EmployeeSalaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_SALARY_ID")
    @EqualsAndHashCode.Include
    private Long employeeSalaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_DETAILS_ID")
    private EmployeeDetailsEntity employeeDetailsEntity;

    @Column(name = "SALARY_DATE")
    private LocalDate salaryDate;

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

    @Column(name = "ACADEMIC_YEAR)")
    private String academicYear;

    @Embedded
    private AuditDetails auditDetails;

}
