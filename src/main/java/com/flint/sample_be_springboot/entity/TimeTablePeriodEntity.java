package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "TIME_TABLE_PERIOD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTablePeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIME_TABLE_PERIOD_ID")
    private Long timeTablePeriodId;

    @ManyToOne
    @JoinColumn(name = "TIME_TABLE_ID", nullable = false)
    private TimeTableEntity timeTableEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY", nullable = false)
    private DayOfWeek day;

    @Column(name = "PERIOD_NUMBER")
    private String periodNumber;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_MASTER_ID")
    private SubjectMasterEntity subjectMasterEntity;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private EmployeeDetailsEntity employeeDetailsEntity;

    @Embedded
    private AuditDetails auditDetails;
}
