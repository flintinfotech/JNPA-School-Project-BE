package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeDetailsDTO {

    private Long employeeDetailsId;
    private Long userId;
    private String employeeCode;
    private String userName;

    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNo;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
    private Role role;

    private String address;

    private String qualification;
    private String specialization;
    private Integer experience;
    private String designation;
    private LocalDate joiningDate;
    private LocalDate leavingDate;


    private String bloodGroup;
    private List<UserDocumentDTO> userDocumentDTOS;

}
