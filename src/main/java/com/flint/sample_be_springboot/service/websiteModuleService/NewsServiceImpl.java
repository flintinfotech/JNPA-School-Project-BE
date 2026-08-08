package com.flint.sample_be_springboot.service.websiteModuleService;

import com.flint.sample_be_springboot.dto.websiteModuleDTOS.NewsDTO;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.NewsEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.websiteModuleRepository.NewsRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NewsServiceImpl extends BaseService implements NewsService {

    @Autowired
    NewsRepository newsRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public NewsDTO getNewsById(Long newsId) {
        log.info("Enter into getNewsById");

        NewsEntity existingNews = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException("News information not found", HttpStatus.NOT_FOUND));

        NewsDTO newsDTO = modelMapper.map(existingNews, NewsDTO.class);

        log.info("Exit from getNewsById");
        return newsDTO;
    }

    @Override
    public NewsDTO saveNews(NewsDTO newsDTO) {
        log.info("Enter into saveNews");

        NewsEntity newsEntity = modelMapper.map(newsDTO, NewsEntity.class);

        newsEntity.setAuditDetails(addAuditDetails(newsEntity.getAuditDetails()));

        if(newsDTO.getNewsData() != null){
            newsEntity.setNewsData(Base64.getDecoder().decode(newsDTO.getNewsData()));
        }

        NewsEntity savedEntity = newsRepository.save(newsEntity);

        NewsDTO savedDto = modelMapper.map(savedEntity, NewsDTO.class);

        log.info("Exit into saveNews");
        return savedDto;
    }

    @Override
    public NewsDTO updateNews(NewsDTO newsDTO) {
        log.info("Enter into updateNews");

        NewsEntity existingNews = newsRepository.findById(newsDTO.getNewsId())
                        .orElseThrow(() -> new CustomException("News information not found", HttpStatus.NOT_FOUND));

        AuditDetails auditDetails = existingNews.getAuditDetails();

        modelMapper.map(newsDTO, existingNews);

        existingNews.setAuditDetails(addAuditDetails(auditDetails));

        if(newsDTO.getNewsData() != null){
            existingNews.setNewsData(Base64.getDecoder().decode(newsDTO.getNewsData()));
        }else{
            existingNews.setNewsData(null);
        }

        NewsEntity updatedEntity = newsRepository.save(existingNews);

        NewsDTO updatedDto = modelMapper.map(updatedEntity, NewsDTO.class);

        log.info("Exit from updateNews");
        return updatedDto;
    }

    @Override
    public String deleteNews(Long newsId) {
        log.info("Enter into deleteNews");

        NewsEntity existingNews = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException("News information not found", HttpStatus.NOT_FOUND));

        newsRepository.delete(existingNews);

        log.info("Enter into deleteNews");

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllNewsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllNewsByFilter");



        Page<NewsEntity> newsEntityPage;
        List<NewsEntity> newsEntityList;
        long totalElements;

        Pageable pageable1 = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "newsId")
        );

        CustomQuerySpecification<NewsEntity> customQuerySpecification =  CustomQuerySpecification.getInstance(filter);

        if(paginate){
            newsEntityPage = newsRepository.findAll(customQuerySpecification, pageable1);
            newsEntityList = newsEntityPage.getContent();
            totalElements = newsEntityPage.getTotalElements();
        }else{
            newsEntityList = newsRepository.findAll(customQuerySpecification);
            totalElements = newsEntityList.size();
        }

        List<NewsDTO> newsDTOS = newsEntityList.stream()
                .map(n -> modelMapper.map(n, NewsDTO.class)).collect(Collectors.toList());

        log.info("Enter into getAllNewsByFilter");
        Map<String, Object> result = new HashMap<>();
        result.put("newsDTOS", newsDTOS);
        result.put("total element", totalElements);
        return result;
    }
}
