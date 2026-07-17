package com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam;

import lombok.Data;

@Data
public class ToppersDTO {

    private Long topperId;
    private Long examId;
    private String section;
    private String medium;
    private String userName;
    private String std;
    private String description;
    private String studentImage;

}
