package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.student.StudentResultDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StudentResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/studentResult")
public class StudentResultController {

    @Autowired
    private StudentResultService studentResultService;

    @PostMapping("/saveStudentResult")
    public ResponseEntity<?> saveStudentResult(@RequestBody StudentResultDTO studentResultDTO) {
        StudentResultDTO data = studentResultService.saveStudentResult(studentResultDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student result saved successfully").data(data).build());
    }


    @GetMapping("/getStudentResultById/{resultId}")
    public ResponseEntity<?> getStudentResultById(@PathVariable Long resultId) {
        StudentResultDTO studentResultDTO = studentResultService.getStudentResultByStudentId(resultId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(studentResultDTO).build());
    }


    @PutMapping("/updateStudentResult")
    public ResponseEntity<?> updateStudentResult(@RequestBody StudentResultDTO studentResultDTO) {
        StudentResultDTO data = studentResultService.updateStudentResult(studentResultDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student result updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteStudentResult/{resultId}")
    public ResponseEntity<?> deleteStudentResult(@PathVariable Long resultId) {
        String msg = studentResultService.deleteStudentResult(resultId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllStudentsResultByFilter")
    public ResponseEntity<?> getAllStudentsResultByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                         @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> data = studentResultService.getAllStudentsResultByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
