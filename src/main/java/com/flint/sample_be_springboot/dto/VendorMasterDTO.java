package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.AuditDetails;
import lombok.Data;

@Data
public class VendorMasterDTO {

    private Long vendorMasterId;
    private String vendorName;
    private String mobileNumber;
    private String address;
    private String pinCode;
    private String panNumber;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String paymentType;
    private String vendorType;
    private String remark;
    private String academicYear;


}
