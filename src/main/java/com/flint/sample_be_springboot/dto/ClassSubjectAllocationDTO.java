package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassSubjectAllocationDTO {

    private Long classSubjectAllocationId;
    private Long classMasterId;
    private List<Long> subjectMasterIds;

}
