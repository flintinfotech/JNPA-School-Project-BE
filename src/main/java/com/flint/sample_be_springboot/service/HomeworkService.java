package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.HomeworkDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface HomeworkService {

    HomeworkDTO getHomeworkById(Long homeworkId);

    HomeworkDTO saveHomework(HomeworkDTO homeworkDTO);

    HomeworkDTO updateHomework(HomeworkDTO homeworkDTO);

    String deleteHomework(Long homeworkId);

    Map<String, Object> getAllHomeworkByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
