package com.flint.sample_be_springboot.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class FormerStudentLCDTO {

    private Long formerStudentLCId;

    private Long formerStudentId;

    // LC details
    private String lcNumber;
    private LocalDate lcDate;

    // School admission / register details
    private String admissionNumber;
    private LocalDate admissionDate;

    // Student details
    private String studentName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String gender;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String nationality;
    private String motherTongue;
    private String religion;
    private String caste;

    // Education details
    private String standardAtLeaving;
    private String division;
    private String medium;
    private String academicYear;

    // Leaving details
    private LocalDate dateOfLeaving;
    private String reasonForLeaving;
    private String result;
    private String conduct;
    private String remark;

}


