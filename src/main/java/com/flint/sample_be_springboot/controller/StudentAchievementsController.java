package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.student.StudentAchievementsDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StudentAchievementsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/studentAchievements")
public class StudentAchievementsController {

    @Autowired
    private StudentAchievementsService studentAchievementsService;


    @PostMapping("/saveStudentAchievements")
    public ResponseEntity<?> saveStudentAchievements(@RequestBody StudentAchievementsDTO studentAchievementsDTO) {

        StudentAchievementsDTO data = studentAchievementsService.saveStudentAchievements(studentAchievementsDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student achievement saved successfully").data(data).build());
    }

    @GetMapping("/getStudentAchievementsById/{studentAchievementId}")
    public ResponseEntity<?> getStudentAchievementsById(@PathVariable Long studentAchievementId) {

        StudentAchievementsDTO studentAchievementsDTO = studentAchievementsService.getStudentAchievementsById(studentAchievementId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(studentAchievementsDTO).build());
    }




    @PutMapping("/updateStudentAchievements")
    public ResponseEntity<?> updateStudentAchievements(@RequestBody StudentAchievementsDTO studentAchievementsDTO) {

        StudentAchievementsDTO data = studentAchievementsService.updateStudentAchievements(studentAchievementsDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Student achievement updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteStudentAchievements/{studentAchievementId}")
    public ResponseEntity<?> deleteStudentAchievements(@PathVariable Long studentAchievementId) {

        String msg = studentAchievementsService.deleteStudentAchievements(studentAchievementId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllStudentAchievementsByFilter")
    public ResponseEntity<?> getAllStudentAchievementsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                               @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = studentAchievementsService.getAllStudentAchievementsByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }
}
