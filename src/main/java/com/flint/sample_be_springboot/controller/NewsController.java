package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.NewsDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;

    @GetMapping("/getNewsById/{newsId}")
    public ResponseEntity<?> getNewsById(@PathVariable Long newsId) {
        NewsDTO newsDTO = newsService.getNewsById(newsId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(newsDTO).build());
    }

    @PostMapping("/saveNews")
    public ResponseEntity<?> saveNews(@RequestBody NewsDTO newsDTO) {
        NewsDTO data = newsService.saveNews(newsDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("News saved successfully").data(data).build());
    }

    @PutMapping("/updateNews")
    public ResponseEntity<?> updateNews(@RequestBody NewsDTO newsDTO) {
        NewsDTO data = newsService.updateNews(newsDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("News updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteNews/{newsId}")
    public ResponseEntity<?> deleteNews(@PathVariable Long newsId) {
        String msg = newsService.deleteNews(newsId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllNewsByFilter")
    public ResponseEntity<?> getAllNewsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = newsService.getAllNewsByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}
