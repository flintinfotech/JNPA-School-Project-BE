package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.HomeworkDTO;
import com.flint.sample_be_springboot.repository.HomeworkRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HomeworkServiceImpl extends BaseService implements HomeworkService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Override
    public HomeworkDTO getHomeworkById(Long homeworkId) {
        log.info("Enter into getHomeworkById");

        log.info("Exit from getHomeworkById");
        return null;
    }

    @Override
    public HomeworkDTO saveHomework(HomeworkDTO homeworkDTO) {
        log.info("Enter into saveHomework");

        log.info("Exit from saveHomework");
        return null;
    }

    @Override
    public HomeworkDTO updateHomework(HomeworkDTO homeworkDTO) {
        log.info("Enter into updateHomework");

        log.info("Exit from updateHomework");
        return null;
    }

    @Override
    public String deleteHomework(Long homeworkId) {
        log.info("Enter into deleteHomework");

        log.info("Exit from deleteHomework");
        return "";
    }

    @Override
    public Map<String, Object> getAllHomeworkByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllHomeworkByFilter");

        log.info("Exit from getAllHomeworkByFilter");
        return Map.of();
    }
}
