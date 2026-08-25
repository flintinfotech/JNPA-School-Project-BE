package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.DayOfWeek;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeTablePeriodDTO {
    private Long timeTablePeriodId;
    private Long timeTableId;
    private DayOfWeek day;
    private Integer periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long subjectId;
    private Long teacherId;
}
