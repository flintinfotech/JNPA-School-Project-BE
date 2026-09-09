package com.flint.sample_be_springboot.dto.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FormerStudentDocumentDTO {

    private Long formerStudentDocumentId;
    private Long formerStudentId;
    private String documentName;
    private LocalDate uploadDate;
    private String document;
    private AuditDetails auditDetails;

}
