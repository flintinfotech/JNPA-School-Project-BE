package com.flint.sample_be_springboot.entity.student;


import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EXAM_SUBJECTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubjectsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXAM_SUBJECTS_ID")
    private Long examSubjectsId;

    @ManyToOne
    @JoinColumn(name = "RESULT_ID", nullable = false)
    private StudentResultEntity studentResult;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @Column(name = "MAXIMUM_MARKS")
    private BigDecimal maximumMarks;

    @Column(name = "OBTAINED_MARKS")
    private BigDecimal obtainedMarks;

    @Column(name = "STATUS")
    private String status;
}
