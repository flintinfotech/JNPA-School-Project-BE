package com.flint.sample_be_springboot.entity.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FORMER_STUDENT_LC_ENTITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FormerStudentLCEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "FORMER_STUDENT_LC_ID")
    private Long formerStudentLCId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORMER_STUDENT_ID", nullable = false, unique = true)
    private FormerStudentEntity formerStudentEntity;

    // LC details
    @Column(name = "LC_NUMBER", nullable = false)
    private String lcNumber;

    @Column(name = "LC_DATE", nullable = false)
    private LocalDate lcDate;

    // School admission / register details
    @Column(name = "ADMISSION_NUMBER")
    private String admissionNumber;

    @Column(name = "ADMISSION_DATE")
    private LocalDate admissionDate;

    // Student details
    @Column(name = "STUDENT_NAME")
    private String studentName;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "MOTHER_NAME")
    private String motherName;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "PLACE_OF_BIRTH")
    private String placeOfBirth;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "MOTHER_TONGUE")
    private String motherTongue;

    @Column(name = "RELIGION")
    private String religion;

    @Column(name = "CASTE")
    private String caste;

    // Education details
    @Column(name = "STANDARD_AT_LEAVING")
    private String standardAtLeaving;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    // Leaving details
    @Column(name = "DATE_OF_LEAVING")
    private LocalDate dateOfLeaving;

    @Column(name = "REASON_FOR_LEAVING")
    private String reasonForLeaving;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "CONDUCT")
    private String conduct;

    @Column(name = "REMARK")
    private String remark;

    @Embedded
    private AuditDetails auditDetails;
}
