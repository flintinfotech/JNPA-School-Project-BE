package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import com.flint.sample_be_springboot.entity.TimeTableEntity;
import com.flint.sample_be_springboot.enums.DayOfWeek;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeTablePeriodDTO {
    private Long timeTablePeriodId;
    private TimeTableEntity timeTableEntity;
    private DayOfWeek day;
    private Integer periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private SubjectMasterEntity subjectMasterEntity;
    private EmployeeDetailsEntity teacher;
}
