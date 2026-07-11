package com.flint.sample_be_springboot.entity.exam;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "EXAM_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "EXAM_ID")
    private Long examId;

    @Column(name = "CLASS_ROOM_NAME")
    private String classRoomName;

    @Column(name = "ACADEMIC_YEAR_NAME")
    private String academicYearName;

    @Column(name = "MEDIUM")
    private String medium;

    @Column(name = "RESULT_10TH")
    private String result10th;

    @Column(name = "RESULT_12TH")
    private String result12th;

    @Column(name = "STUDENT_SCORING_90")
    private String studentScoring90;

    @Column(name = "UNIVERSITY_RANK")
    private String universityRank;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "examEntity", fetch = FetchType.LAZY)
    private List<ExamNoticeEntity> examNotices;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "examEntity", fetch = FetchType.LAZY)
    private List<ExamResultEntity> examResults;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "examEntity", fetch = FetchType.LAZY)
    private List<ToppersEntity> toppersEntities;

    @Embedded
    private AuditDetails auditDetails;

}
