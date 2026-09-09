package com.flint.sample_be_springboot.entity.formerStudent;


import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "FORMER_EXAM_SUBJECTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormerExamSubjectsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FORMER_EXAM_SUBJECTS_ID")
    private Long formerExamSubjectsId;

    @ManyToOne
    @JoinColumn(name = "FORMER_STUDENT_RESULT_ID", nullable = false)
    private FormerStudentResultEntity formerStudentResultEntity;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @Column(name = "MAXIMUM_MARKS")
    private BigDecimal maximumMarks;

    @Column(name = "OBTAINED_MARKS")
    private BigDecimal obtainedMarks;

    @Column(name = "STATUS")
    private String status;

    @Embedded
    private AuditDetails auditDetails;
}

