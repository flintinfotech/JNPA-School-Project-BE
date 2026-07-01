package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentService {

    StudentDTO saveStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(StudentDTO studentDTO);

    StudentDTO getStudentById(Long studentId);

    String deleteStudent(Long studentId);

    Map<String, Object> getAllStudentsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
