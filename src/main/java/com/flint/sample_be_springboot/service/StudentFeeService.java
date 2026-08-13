package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentDTO;
import com.flint.sample_be_springboot.dto.student.StudentFeeDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentFeeService {

    StudentFeeDTO getStudentFee(Long studentFeeId);

    Map<String, Object> getAllStudentsFeeByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

    StudentFeeDTO saveStudentFee(StudentFeeDTO studentFeeDTO);


    String deleteStudentFeeById(Long id);

    StudentDTO updateStudentFee(StudentFeeDTO studentFeeDTO);
}


//shift + f6 --- to rename from all the method

