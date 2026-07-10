package com.flint.sample_be_springboot.dto.exam;

import com.flint.sample_be_springboot.entity.exam.ExamEntity;
import lombok.Data;

@Data
public class ExamResultDTO {

    private Long examResultEntity;
    private ExamEntity examEntity;
    private String resultName;
    private String resultData;

}
