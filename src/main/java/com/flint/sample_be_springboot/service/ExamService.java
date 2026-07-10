package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.exam.ExamDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ExamService {

    ExamDTO getExamById(Long examId);

    ExamDTO saveExam(ExamDTO examDTO);

    ExamDTO updateExam(ExamDTO examDTO);

    String deleteExam(Long examId);

    Map<String, Object> getAllExamsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
