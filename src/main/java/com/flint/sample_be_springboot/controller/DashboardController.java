package com.flint.sample_be_springboot.controller;


import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/getAllStudentsCount")
    public ResponseEntity<?> getAllStudentsCount() {
        Long tetalCount = dashboardService.getAllStudentsCount();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(tetalCount).build());
    }

    @GetMapping("/getAllTeachers")
    public ResponseEntity<?> getAllTeachers()
    {
       Map<String, Long> map = dashboardService.getAllTeachers();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }


}
