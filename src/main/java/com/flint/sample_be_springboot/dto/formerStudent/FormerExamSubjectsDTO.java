package com.flint.sample_be_springboot.dto.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FormerExamSubjectsDTO {

    private Long examSubjectsId;
    private Long formerResultId;
    private String subjectName;
    private BigDecimal maximumMarks;
    private BigDecimal obtainedMarks;
    private String status;
    private AuditDetails auditDetails;

}
