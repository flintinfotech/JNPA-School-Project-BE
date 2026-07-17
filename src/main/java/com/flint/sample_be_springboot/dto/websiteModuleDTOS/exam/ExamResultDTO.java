package com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam;

import lombok.Data;

@Data
public class ExamResultDTO {

    private Long examResultId;
    private Long examId;
    private String resultName;
    private String resultData;

}
