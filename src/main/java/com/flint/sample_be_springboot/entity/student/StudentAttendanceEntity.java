package com.flint.sample_be_springboot.entity.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.StudentAttendanceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Table(name = "STUDENT_ATTENDANCE_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentAttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "STUDENT_ATTENDANCE_ID")
    private Long studentAttendanceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID",referencedColumnName = "STUDENT_ID",nullable = false)
    private StudentEntity studentEntity;

    @NotNull
    @Column(name = "DATE")
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ATTENDANCE_STATUS")
    private StudentAttendanceStatus attendanceStatus;

    @Column(name = "ACADEMIC_YEAR")
    private String academicYear;

    @Embedded
    private AuditDetails auditDetails;

}
