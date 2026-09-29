package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.EmployeeSalaryDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("employeeSalary")
public class EmployeeSalaryController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @PostMapping("/saveEmployeeSalary")
    public ResponseEntity<?> saveEmployeeSalary(@RequestBody EmployeeSalaryDTO employeeSalaryDTO) {
        EmployeeSalaryDTO salaryDTO = employeeSalaryService.saveEmployeeSalary(employeeSalaryDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Employee salary saved successfully").data(salaryDTO).build());
    }

    @GetMapping("/getEmployeeSalary/{employeeSalaryId}")
    public ResponseEntity<?> getEmployeeSalary(@PathVariable Long employeeSalaryId) {
        EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryService.getEmployeeSalary(employeeSalaryId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(employeeSalaryDTO).build());
    }

    @PutMapping("/updateEmployeeSalary")
    public ResponseEntity<?> updateEmployeeSalary(@RequestBody EmployeeSalaryDTO employeeSalaryDTO) {
        EmployeeSalaryDTO data = employeeSalaryService.updateEmployeeSalary(employeeSalaryDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Employee salary updated successfully").data(data).build());
    }

    @DeleteMapping("/deleteEmployeeSalary/{employeeSalaryId}")
    public ResponseEntity<?> deleteEmployeeSalary(@PathVariable Long employeeSalaryId) {
        String msg = employeeSalaryService.deleteEmployeeSalary(employeeSalaryId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllEmployeeSalaryByFilter")
    public ResponseEntity<?> getAllEmployeeSalaryByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                          @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = employeeSalaryService.getAllEmployeeSalaryByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
