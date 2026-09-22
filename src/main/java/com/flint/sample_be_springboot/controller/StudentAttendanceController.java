package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.student.StudentAttendanceDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StudentAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/studentAttendance")
public class StudentAttendanceController {

    @Autowired
    private StudentAttendanceService studentAttendanceService;

    @GetMapping("/getStudentAttendanceById/{studentAttendanceId}")
    public ResponseEntity<?> getStudentAttendanceById(@PathVariable Long studentAttendanceId) {
        StudentAttendanceDTO data = studentAttendanceService.getStudentAttendanceById(studentAttendanceId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record fetched successfully").data(data).build());
    }

    @PostMapping("/saveStudentAttendance")
    public ResponseEntity<?> saveStudentAttendance(@RequestBody StudentAttendanceDTO studentAttendanceDTO) {
        StudentAttendanceDTO data = studentAttendanceService.saveStudentAttendance(studentAttendanceDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record saved successfully").data(data).build());
    }

    @PutMapping("/updateStudentAttendance")
    public ResponseEntity<?> updateStudentAttendance(@RequestBody StudentAttendanceDTO studentAttendanceDTO) {
        StudentAttendanceDTO data = studentAttendanceService.updateStudentAttendance(studentAttendanceDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record updated successfully").data(data).build());
    }

    @DeleteMapping("/deleteStudentAttendance/{studentAttendanceId}")
    public ResponseEntity<?> deleteStudentAttendance(@PathVariable Long studentAttendanceId) {
        String msg = studentAttendanceService.deleteStudentAttendance(studentAttendanceId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record deleted successfully").data(msg).build());
    }

    @PostMapping("/getAllStudentAttendanceByFilter")
    public ResponseEntity<?> getAllStudentAttendanceByFilter(@RequestBody Map<String, Object> filterBody,
                                                             Pageable pageable, boolean paginate) {
        Map<String, Object> data = studentAttendanceService.getAllStudentAttendanceByFilter(filterBody, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Records fetched successfully").data(data).build());
    }

}
