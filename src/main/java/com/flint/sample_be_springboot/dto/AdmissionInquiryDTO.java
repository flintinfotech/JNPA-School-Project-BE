package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.StudentStatus;
import lombok.Data;

@Data
public class AdmissionInquiryDTO {

    private Long admissionInquiryId;
    private  String firstName;
    private  String lastName;
    private  String contactNumber;
    private  String standard;
    private  String medium;
    private  String stream;
    private String status;

}
