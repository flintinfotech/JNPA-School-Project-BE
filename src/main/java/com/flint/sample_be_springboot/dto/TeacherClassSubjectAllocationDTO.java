package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import lombok.Data;

import java.util.List;

@Data
public class TeacherClassSubjectAllocationDTO {

    private Long teacherClassSubjectAllocationId;
    private Long userInformationId;
    private Long classMasterId;
    private List<Long> subjectIds;
}
