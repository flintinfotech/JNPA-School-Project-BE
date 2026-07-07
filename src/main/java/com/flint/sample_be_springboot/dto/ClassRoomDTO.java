package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

import java.util.List;

@Data
public class ClassRoomDTO {

    private Long classRoomId;
    private String classRoomName;
    private String academicYearName;
    private String description;
    private String medium;

    private List<SubjectDTO> subjectDTOList;
    private String brochure;

    private AuditDetails auditDetails;

}
