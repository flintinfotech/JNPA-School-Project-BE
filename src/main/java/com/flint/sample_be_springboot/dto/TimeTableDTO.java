package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimeTableDTO {

    private Long timeTableId;
    private String academicYear;
    private Long classMasterId;
    private String division;
    private List<TimeTablePeriodDTO> timeTablePeriods;
}
