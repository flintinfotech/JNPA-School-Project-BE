package com.flint.sample_be_springboot.dto.websiteModuleDTOS;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsDTO {

    private Long newsId;
    private String news;
    private String newsDescription;
    private String newsData;
    private LocalDate eventDate;

}
