package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.exam.ExamDTO;
import com.flint.sample_be_springboot.repository.ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ExamServiceImpl implements ExamService{

    @Autowired
    ExamRepository examRepository;

    @Override
    public ExamDTO getExamById(Long examId) {
        log.info("Enter into getExamById");



        log.info("Exit from getExamById");
        return null;
    }

    @Override
    public ExamDTO saveExam(ExamDTO examDTO) {
        log.info("Enter into saveExam");



        log.info("Exit from saveExam");
        return null;
    }

    @Override
    public ExamDTO updateExam(ExamDTO examDTO) {
        return null;
    }

    @Override
    public String deleteExam(Long examId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllExamsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
