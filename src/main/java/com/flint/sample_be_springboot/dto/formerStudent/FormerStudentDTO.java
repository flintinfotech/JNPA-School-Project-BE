package com.flint.sample_be_springboot.dto.formerStudent;

import com.flint.sample_be_springboot.dto.FormerStudentLCDTO;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.FeePayment;
import com.flint.sample_be_springboot.enums.StudentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FormerStudentDTO {

    private Long formerStudentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String studentCode;
    private String gender;
    private String DOB;
    private String aadhaarCard;
    private String motherName;
    private String phone;
    private String address;
    private String bloodGroup;
    private String category;
    private String religion;
    private String caste;
    private String nationality;
    private StudentStatus status;
    private String admissionNo;
    private List<FormerStudentResultDTO> formerStudentResultDTOS;
    private List<FormerStudentDocumentDTO> formerStudentDocuments;
    private AuditDetails auditDetails;
    private String profileImg;
    private FeePayment paymentStatus;
    private BigDecimal totalFeeAmount;
    private BigDecimal pendingFeeAmount;
    private FormerStudentLCDTO formerStudentLCDTO;

}
