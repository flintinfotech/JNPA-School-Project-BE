package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.EmployeeDetailsDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.EmployeeDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employeeDetails")
public class EmployeeDetailsController {

    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @GetMapping("/getEmployeeDetailsById/{userId}")

    public ResponseEntity<?> getEmployeeDetailsById(@PathVariable Long userId) {

        EmployeeDetailsDTO employeeDetailsDTO = employeeDetailsService.getEmployeeDetailsById(userId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(employeeDetailsDTO).build());
    }

    @GetMapping("/getEmployeeDetailsByEmployeeId/{employeeId}")
    public ResponseEntity<?> getEmployeeDetailsByEmployeeId(@PathVariable Long employeeId) {

        EmployeeDetailsDTO employeeDetailsDTO = employeeDetailsService.getEmployeeDetailsByEmployeeId(employeeId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(employeeDetailsDTO).build());
    }

    @PostMapping("/saveEmployeeDetails")
    public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeDetailsDTO employeeDetailsDTO) {

        EmployeeDetailsDTO data = employeeDetailsService.saveEmployeeDetails(employeeDetailsDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Employee information saved successfully").data(data).build());
    }

    @PutMapping("/updateEmployeeDetails")
    public ResponseEntity<?> updateEmployeeDetails(@RequestBody EmployeeDetailsDTO employeeDetailsDTO) {

        EmployeeDetailsDTO data = employeeDetailsService.updateEmployeeDetails(employeeDetailsDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Employee information updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteEmployeeDetails/{employeeDetailsId}")
    public ResponseEntity<?> deleteEmployeeDetails(@PathVariable Long employeeDetailsId) {

        String msg = employeeDetailsService.deleteEmployeeDetails(employeeDetailsId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllEmployeeDetailsByFilter")
    public ResponseEntity<?> getAllEmployeeDetailsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                           @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = employeeDetailsService.getAllEmployeeDetailsByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}