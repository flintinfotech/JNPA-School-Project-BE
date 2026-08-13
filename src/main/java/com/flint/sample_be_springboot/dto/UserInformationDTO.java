package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.UserDocumentEntity;
import com.flint.sample_be_springboot.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserInformationDTO {

    private Long userInformationId;
    private Long userId;
    private String employeeCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
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
