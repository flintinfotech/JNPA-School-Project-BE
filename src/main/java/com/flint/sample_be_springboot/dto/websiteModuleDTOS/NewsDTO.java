package com.flint.sample_be_springboot.dto.websiteModuleDTOS;

import lombok.Data;

@Data
public class NewsDTO {

    private Long newsId;
    private String news;
    private String newsDescription;
    private String newsData;

}
