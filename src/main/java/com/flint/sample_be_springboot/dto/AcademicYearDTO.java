package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.SubScreenEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AcademicYearDTO {

    private Long academicYearId;
    private Long classRoomId;
    private String academicYearName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;

    private String cbseAffiliated;
    private String avgPassingPercentage;
    private String subjectOffered;
    private String studentTeacherRatio;

    private List<SubScreenDTO> subScreenDTOS;

    private AuditDetails auditDetails;

}
