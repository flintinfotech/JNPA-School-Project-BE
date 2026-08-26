package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STUDENT_ACHIEVEMENTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentAchievementsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="STUDENT_ACHIEVEMENT_ID")
    private Long studentAchievementId;

    @Column(name = "ACHIEVEMENT_NAME")
    private String achievementName;

    @Column(name = "ACHIEVEMENT_DESCRIPTION")
    private String achievementDescription;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private StudentEntity studentEntity;

    @Embedded
    private AuditDetails auditDetails;


}
