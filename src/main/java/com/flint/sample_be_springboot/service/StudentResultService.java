package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentResultDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentResultService {

    StudentResultDTO saveStudentResult(StudentResultDTO studentResultDTO);

    StudentResultDTO updateStudentResult(StudentResultDTO studentResultDTO);

    StudentResultDTO getStudentResultByStudentId(Long resultId);

    String deleteStudentResult(Long resultId);

    Map<String, Object> getAllStudentResultByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
