package com.flint.sample_be_springboot.dto.student;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParentDTO {

    private Long parentId;
    private Long studentId;
    private String name;
    private String relation;
    private String occupation;
    private String phone;
    private String email;
    private String address;
    private BigDecimal annualIncome;

}
