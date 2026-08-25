package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentDTO;
import com.flint.sample_be_springboot.dto.student.StudentFeeDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentFeeService {

    StudentFeeDTO getStudentFee(Long studentFeeId);

    StudentFeeDTO saveStudentFee(StudentFeeDTO studentFeeDTO);

    StudentFeeDTO updateStudentFee(StudentFeeDTO studentFeeDTO);

    String deleteStudentFeeById(Long id);

    Map<String, Object> getAllStudentsFeeByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}

