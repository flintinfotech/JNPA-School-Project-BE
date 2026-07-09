package com.flint.sample_be_springboot.entity.classRoom;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Table(name = "ACADEMIC_YEAR_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AcademicYearEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "ACADEMIC_YEAR_ID")
    private Long academicYearId;

    @ManyToOne(targetEntity = ClassRoomEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASS_ROOM_ID")
    private ClassRoomEntity classRoomEntity;

    @Column(name = "ACADEMIC_YEAR_NAME")
    private String academicYearName;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "IS_CURRENT")
    private Boolean isCurrent;

    @Column(name = "CBSE_AFFILIATED")
    private String cbseAffiliated;

    @Column(name = "AVG_PASSING_PERCENTAGE")
    private String avgPassingPercentage;

    @Column(name = "SUBJECTS_OFFERED")
    private String subjectOffered;

    @Column(name = "STUDENT_TEACHER_RATIO")
    private String studentTeacherRatio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "academicYearEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubScreenEntity> subScreenEntities;

    @Embedded
    private AuditDetails auditDetails;

}
