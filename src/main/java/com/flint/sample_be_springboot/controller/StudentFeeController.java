package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.student.StudentDTO;
import com.flint.sample_be_springboot.dto.student.StudentFeeDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StudentFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/studentFee")
public class StudentFeeController {

    @Autowired
    private StudentFeeService studentFeeService;

    @GetMapping("/getStudentFee/{studentFeeId}")
    public ResponseEntity<?> getStudentFee(@PathVariable Long studentFeeId) {
        StudentFeeDTO data = studentFeeService.getStudentFee(studentFeeId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

    @PostMapping("/saveStudentFee")
    public ResponseEntity<?> saveStudentFee(@RequestBody StudentFeeDTO studentFeeDTO) {
        StudentFeeDTO data = studentFeeService.saveStudentFee(studentFeeDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(data).build());
    }

    @PutMapping("/updateStudentFeeById/{studentFeeId}")
    public ResponseEntity<?> updateStudentFeeById(@RequestBody StudentFeeDTO studentFeeDTO) {
        StudentDTO data = studentFeeService.updateStudentFeeById(studentFeeDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(data).build());
    }

    @DeleteMapping("/deleteStudentFeeById/{studentFeeId")
    public ResponseEntity<?> deleteStudentFeeById(@PathVariable Long studentFeeId) {
        StudentFeeDTO data = studentFeeService.deleteStudentFeeById(studentFeeId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(data).build());
    }


    @PostMapping("/getAllStudentsFeeByFilter")
    public ResponseEntity<?> getAllStudentsFeeByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                       @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> data = studentFeeService.getAllStudentsFeeByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
