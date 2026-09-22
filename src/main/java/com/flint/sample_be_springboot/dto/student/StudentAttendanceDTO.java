package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.enums.StudentAttendanceStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentAttendanceDTO {

    private Long studentAttendanceId;
    private Long studentId;
    private LocalDate attendanceDate;
    private StudentAttendanceStatus attendanceStatus;
    private String academicYear;

}
