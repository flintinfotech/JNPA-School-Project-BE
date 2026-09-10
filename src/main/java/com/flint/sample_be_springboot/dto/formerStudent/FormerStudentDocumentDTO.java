package com.flint.sample_be_springboot.dto.formerStudent;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.FormerStudentDocumentStatus;
import com.flint.sample_be_springboot.enums.FormerStudentDocumentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FormerStudentDocumentDTO {

    private Long formerStudentDocumentId;
    private Long formerStudentId;
    private String documentName;
    private FormerStudentDocumentType documentType;
    private String academicYear;
    private String standard;
    private LocalDate documentDate;
    private FormerStudentDocumentStatus documentStatus;
    private LocalDate uploadDate;
    private LocalDate collectedDate;
    private String collectedBy;
    private String collectedRelation;
    private String remark;
    private String document;
    private AuditDetails auditDetails;

}
