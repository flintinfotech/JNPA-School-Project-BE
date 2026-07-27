package com.flint.sample_be_springboot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicWorkYearDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    public AcademicWorkYearDTO(){}

    public AcademicWorkYearDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
