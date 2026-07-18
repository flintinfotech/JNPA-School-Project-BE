package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassAllocationDTO {

    private Long classSubjectAllocationId;
    private Long classMasterId;
    private List<Long> subjectMasterIds;

}
