package com.flint.sample_be_springboot.dto;

import lombok.Data;

@Data
public class ClassRoomDTO {

    private Long classRoomId;
    private String classRoomName;
    private String academicYearName;
    private String description;

}
