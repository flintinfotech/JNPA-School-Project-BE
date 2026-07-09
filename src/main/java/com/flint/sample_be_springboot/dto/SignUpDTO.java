package com.flint.sample_be_springboot.dto;

import com.flint.sample_be_springboot.enums.Role;
import lombok.Data;

@Data
public class SignUpDTO {

    private Long userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String mobileNo;
    private String email;
    private String section;
    private String medium;

}
