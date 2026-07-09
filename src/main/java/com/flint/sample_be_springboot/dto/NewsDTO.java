package com.flint.sample_be_springboot.dto;

import lombok.Data;

@Data
public class NewsDTO {

    private Long newsId;
    private String news;
    private String newsData;

}
