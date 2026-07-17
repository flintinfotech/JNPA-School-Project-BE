package com.flint.sample_be_springboot.dto.websiteModuleDTOS.classRoom;

import lombok.Data;

@Data
public class SubjectDTO {

    private Long subjectId;
    private Long classRoomId;
    private String subjectName;
    private String subjectDescription;

}
