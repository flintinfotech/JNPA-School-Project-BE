package com.flint.sample_be_springboot.entity;

import com.flint.sample_be_springboot.enums.AcademicCalendarEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "ACADEMIC_CALENDAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACADEMIC_CALENDAR_ID")
    private Long academicCalendarId;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Column(name = "EVENT_TITLE", nullable = false)
    private String eventTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "EVENT_TYPE", nullable = false)
    private AcademicCalendarEventType eventType;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    private AuditDetails auditDetails;

}
