package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDocumentDTO {

    private Long studentDocumentId;
    private Long studentId;
    private String documentName;
    private LocalDate uploadDate;
    private String document;
    private AuditDetails auditDetails;

}
