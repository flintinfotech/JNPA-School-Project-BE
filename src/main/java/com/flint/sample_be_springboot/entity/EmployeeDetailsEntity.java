package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.Role;
import com.flint.sample_be_springboot.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EMPLOYEE_DETAILS_ENTITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "EMPLOYEE_DETAILS_ID")
    private Long employeeDetailsId;

    @OneToOne(mappedBy = "employeeDetails", fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @NotNull
    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @NotNull
    @Size(max = 50)
    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

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

    @Column(name = "LEAVING_DATE")
    private LocalDate leavingDate;

    @Column(name = "BLOOD_GROUP")
    private String bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private StudentStatus status;

    @OneToMany(mappedBy = "employeeDetailsEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserDocumentEntity> userDocumentEntities;

    @OneToMany(mappedBy = "employeeDetailsEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = new ArrayList<>();

    @Embedded
    private AuditDetails auditDetails;
}