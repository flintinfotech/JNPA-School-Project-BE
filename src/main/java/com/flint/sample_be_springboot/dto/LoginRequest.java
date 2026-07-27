package com.flint.sample_be_springboot.dto;

import lombok.Data;

@Data
public class LoginRequest {
    public String username;
    public String password;
    public AcademicWorkYearDTO academicWorkYearDTO;
}
