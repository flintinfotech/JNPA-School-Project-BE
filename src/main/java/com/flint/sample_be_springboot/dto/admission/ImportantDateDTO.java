package com.flint.sample_be_springboot.dto.admission;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ImportantDateDTO {

    private Long importantDateId;
    private Long admissionId;
    private String eventName;
    private LocalDate eventDate;

}
