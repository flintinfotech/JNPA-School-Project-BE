package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Table(name = "ACADEMIC_INFORMATION_Entity")
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
    private Long admissionNo;

    @ManyToOne(targetEntity = StudentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @Column(name = "ADMISSION_DATE")
    private LocalDate admissionDate;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "SECTION")
    private String section;

    @Column(name = "ROLL_NO")
    private String rollNo;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

}
