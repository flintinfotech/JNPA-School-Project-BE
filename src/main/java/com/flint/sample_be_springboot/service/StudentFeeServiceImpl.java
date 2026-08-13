package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentDTO;
import com.flint.sample_be_springboot.dto.student.StudentFeeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentFeeServiceImpl implements StudentFeeService {

    @Override
    public StudentFeeDTO getStudentFee(Long studentFeeId) {
        return null;
    }

    @Override
    public Map<String, Object> getAllStudentsFeeByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }

    @Override
    public StudentFeeDTO saveStudentFee(StudentFeeDTO studentFeeDTO) {
        return null;
    }

    @Override
    public String deleteStudentFeeById(Long id) {
        return null;
    }

    @Override
    public StudentDTO updateStudentFee(StudentFeeDTO studentFeeDTO) {
        return null;
    }
}
