package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassMasterDTO {

    private Long classMasterId;
    private String standard;
    private String division;
    private String medium;

}
