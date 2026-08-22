package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Table(name = "ACADEMIC_INFORMATION_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AcademicInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "ACADEMIC_INFORMATION_ID")
    private Long academicInformationId;

    @NotNull
    @Column(name = "ADMISSION_NO")
    private String admissionNo;   // change to Long -->  String

    @ManyToOne(targetEntity = StudentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @Column(name = "ADMISSION_DATE")
    private LocalDate admissionDate;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "ROLL_NO")
    private String rollNo;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Embedded
    private AuditDetails auditDetails;

}
