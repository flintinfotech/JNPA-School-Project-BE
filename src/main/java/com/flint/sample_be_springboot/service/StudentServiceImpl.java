package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.StudentDTO;
import com.flint.sample_be_springboot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        return null;
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        return null;
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        return null;
    }

    @Override
    public String deleteStudent(Long studentId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllStudentsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }

}
