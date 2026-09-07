package com.flint.sample_be_springboot.controller;


import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/getAllStudentsCount")
    public ResponseEntity<?> getAllStudentsCount() {
        Map<String, Map<String, Long>> studentsCount = dashboardService.getAllStudentsCount();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(studentsCount).build());
    }

    @GetMapping("/getAllUsersCount")
    public ResponseEntity<?> getAllUsersCount() {
        Map<String, Long> map = dashboardService.getAllUsersCount();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

    @GetMapping("/getAllAdmissionInquiryCount")
    public ResponseEntity<?> getAllAdmissionInquiryCount() {
        Map<String, Long> map = dashboardService.getAllAdmissionInquiryCount();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

    @GetMapping("/getAllExpensesCount/{academicYear}")
    public ResponseEntity<?> getAllExpensesCount(@PathVariable String academicYear) {
        Map<String, Long> map = dashboardService.getAllExpensesCount(academicYear);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

    @GetMapping("/getAllPaidExpensesTotal/{academicYear}")
    public ResponseEntity<?> getAllPaidExpensesTotal(@PathVariable String academicYear) {
        Map<String, BigDecimal> map = dashboardService.getAllPaidExpensesTotal(academicYear);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

    @GetMapping("/getAllExpensesTotal/{academicYear}")
    public ResponseEntity<?> getAllExpensesTotal(@PathVariable String academicYear) {
        Map<String, BigDecimal> map = dashboardService.getAllExpensesTotal(academicYear);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

    @GetMapping("/getAllTotalPaidExpensesCountAndTotalExpensesCount/{academicYear}")
    public ResponseEntity<?> getAllTotalPaidExpensesCountAndTotalExpensesCount(@PathVariable String academicYear) {
        Map<String, Long> map = dashboardService.getAllTotalPaidExpensesCountAndTotalExpensesCount(academicYear);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }


}
