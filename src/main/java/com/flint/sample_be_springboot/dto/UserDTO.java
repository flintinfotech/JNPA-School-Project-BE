package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private Role role;
    private String mobileNo;
    private String email;
    private AuditDetails auditDetails;

}
