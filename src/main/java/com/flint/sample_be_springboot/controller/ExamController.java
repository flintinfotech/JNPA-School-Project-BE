package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.exam.ExamDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/getExamById/{examId}")
    public ResponseEntity<?> getExamById(@PathVariable Long examId) {
        ExamDTO examDTO = examService.getExamById(examId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(examDTO).build());
    }

    @PostMapping("/saveExam")
    public ResponseEntity<?> saveExam(@RequestBody ExamDTO examDTO) {
        ExamDTO data = examService.saveExam(examDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Exam saved successfully").data(data).build());
    }

    @PutMapping("/updateExam")
    public ResponseEntity<?> updateExam(@RequestBody ExamDTO examDTO) {
        ExamDTO data = examService.updateExam(examDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Exam updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteExam/{examId}")
    public ResponseEntity<?> deleteExam(@PathVariable Long examId) {
        String msg = examService.deleteExam(examId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllExamsByFilter")
    public ResponseEntity<?> getAllExamsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                 @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = examService.getAllExamsByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}
