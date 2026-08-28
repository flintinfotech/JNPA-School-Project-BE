package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.DayOfWeek;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeTablePeriodDTO {
    private Long timeTablePeriodId;
    private Long timeTableId;
    private DayOfWeek day;
    private String periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long subjectId;
    private SubjectMasterDTO subjectMasterDTO;
    private Long employeeDetailsId;
    private EmployeeDetailsDTO employeeDetailsDTO;
}
