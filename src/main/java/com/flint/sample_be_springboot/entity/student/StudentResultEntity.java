package com.flint.sample_be_springboot.entity.student;


import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "STUDENT_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESULT_ID")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private StudentEntity student;

    @Column(name = "STANDARD")
    private String standard;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "EXAM_TYPE")
    private String examType;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @OneToMany(mappedBy = "studentResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSubjectsEntity> examSubjectsEntities;

    @Column(name = "TOTAL_MARKS")
    private BigDecimal totalMarks;

    @Column(name = "OBTAINED_MARKS")
    private BigDecimal obtainedMarks;

    @Column(name = "PERCENTAGE")
    private BigDecimal percentage;

    @Column(name = "GRADE")
    private String grade;

    @Column(name = "RESULT_STATUS")
    private String resultStatus;

    @Embedded
    private AuditDetails auditDetails;
}
