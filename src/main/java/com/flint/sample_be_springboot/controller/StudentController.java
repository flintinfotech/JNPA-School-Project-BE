package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.StudentDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getStudentById/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        StudentDTO studentDTO = studentService.getStudentById(studentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(studentDTO).build());
    }

    @PostMapping("/saveStudent")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO data = studentService.saveStudent(studentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student saved successfully").data(data).build());
    }

    @PutMapping("/updateStudent")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO data = studentService.updateStudent(studentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        String msg = studentService.deleteStudent(studentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllStudentsByFilter")
    public ResponseEntity<?> getAllStudentsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                    @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = studentService.getAllStudentsByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}
