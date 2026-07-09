package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.NewsDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface NewsService {

    NewsDTO getNewsById(Long newsId);

    NewsDTO saveNews(NewsDTO newsDTO);

    NewsDTO updateNews(NewsDTO newsDTO);

    String deleteNews(Long newsId);

    Map<String, Object> getAllNewsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
