package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentAttendanceDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentAttendanceService {

    StudentAttendanceDTO getStudentAttendanceById(Long studentAttendanceId);

    StudentAttendanceDTO saveStudentAttendance(StudentAttendanceDTO studentAttendanceDTO);

    StudentAttendanceDTO updateStudentAttendance(StudentAttendanceDTO studentAttendanceDTO);

    String deleteStudentAttendance(Long studentAttendanceDTO);

    Map<String, Object> getAllStudentAttendanceByFilter(Map<String, Object> filterBody, Pageable pageable, boolean paginate);

}
