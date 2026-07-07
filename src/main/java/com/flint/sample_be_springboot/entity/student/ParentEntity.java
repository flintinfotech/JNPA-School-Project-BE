package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "PARENT_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "PARENT_ID")
    private Long parentId;

    @ManyToOne(targetEntity = StudentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "RELATION")
    private String relation;

    @Column(name = "OCCUPATION")
    private String occupation;

    @Column(name = "PHONE")
    private String phone;

    @Size(max = 50)
    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ANNUAL_INCOME")
    private BigDecimal annualIncome;

    @Embedded
    private AuditDetails auditDetails;

}
