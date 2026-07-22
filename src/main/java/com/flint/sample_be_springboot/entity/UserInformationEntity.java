package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER_INFORMATION_ENTITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "USER_INFORMATION_ID")
    private Long userInformationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", unique = true)
    private UserEntity userEntity;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "ADDRESS", length = 1000)
    private String address;

    @Column(name = "QUALIFICATION")
    private String qualification;

    @Column(name = "SPECIALIZATION")
    private String specialization;

    @Column(name = "EXPERIENCE")
    private Integer experience;

    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "JOINING_DATE")
    private LocalDate joiningDate;

    @Column(name = "BLOOD_GROUP")
    private String bloodGroup;

    @OneToMany(mappedBy = "userInformationEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserDocumentEntity> userDocumentEntities;

    @OneToMany(mappedBy = "userInformationEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = new ArrayList<>();

    @Embedded
    private AuditDetails auditDetails;
}