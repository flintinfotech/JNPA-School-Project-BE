package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HomeworkDTO {

    private Long homeworkId;
    private String subject;
    private String standard;
    private String division;
    private String medium;
    private String academicYear;
    private LocalDate homeworkDate;
    private String remark;
    private String uploadedFile;

}
