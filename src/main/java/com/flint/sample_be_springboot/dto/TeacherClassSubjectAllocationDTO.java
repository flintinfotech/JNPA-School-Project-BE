package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherClassSubjectAllocationDTO {

    private Long teacherClassSubjectAllocationId;
    private Long employeeDetailsId;
    private Long classMasterId;
    private List<Long> subjectIds;
}
