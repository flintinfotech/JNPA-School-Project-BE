package com.flint.sample_be_springboot.dto.student;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.StudentStatus;
import lombok.Data;

import java.util.List;

@Data
public class StudentDTO {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String studentCode;
    private String gender;
    private String DOB;
    private String aadhaarCard;
    private String phone;
    private String address;
    private String bloodGroup;
    private String category;
    private String religion;
    private String caste;
    private String nationality;
    private StudentStatus status;
    private ParentDTO parentDTO;
    private List<StudentDocumentDTO> studentDocuments;
    private List<AcademicInformationDTO> academicInformation;
    private List<StudentResultDTO> studentResultDTOS;
    private List<StudentAchievementsDTO> studentAchievementsDTOS;


    private AuditDetails auditDetails;

    private String profileImg;

}
