package com.flint.sample_be_springboot.dto.student;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamSubjectsDTO {

    private Long ExamSubjectsId;
    private Long resultId;
    private Long subject;
    private BigDecimal maximumMarks;
    private BigDecimal obtainedMarks;
    private String status;
}
