package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import com.flint.sample_be_springboot.entity.TimeTablePeriodEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TimeTableDTO {
    private Long timeTableId;
    private String academicYear;
    private ClassMasterEntity classMasterEntity;
    private String division;
    private List<TimeTablePeriodEntity> periods = new ArrayList<>();
}
