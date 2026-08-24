package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicCalendarDTO {

    private Long academicCalendarId;
    private String eventTitle;
    private String eventType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

}
