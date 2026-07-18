package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.SubjectMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/subjectMaster")
public class SubjectMasterController {

    @Autowired
    private SubjectMasterService subjectMasterService;

    @GetMapping("/getSubjectMasterById/{subjectMasterId}")
    public ResponseEntity<?> getSubjectMasterById(@PathVariable Long subjectMasterId) {

        SubjectMasterDTO subjectMasterDTO = subjectMasterService.getSubjectMasterById(subjectMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(subjectMasterDTO)
                        .build());
    }

    @PostMapping("/saveSubjectMaster")
    public ResponseEntity<?> saveSubjectMaster(@RequestBody SubjectMasterDTO subjectMasterDTO) {

        SubjectMasterDTO data = subjectMasterService.saveSubjectMaster(subjectMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Subject saved successfully")
                        .data(data)
                        .build());
    }

    @PutMapping("/updateSubjectMaster")
    public ResponseEntity<?> updateSubjectMaster(@RequestBody SubjectMasterDTO subjectMasterDTO) {

        SubjectMasterDTO data = subjectMasterService.updateSubjectMaster(subjectMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Subject updated successfully")
                        .data(data)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSubjectMaster/{subjectMasterId}")
    public ResponseEntity<?> deleteSubjectMaster(@PathVariable Long subjectMasterId) {

        String msg = subjectMasterService.deleteSubjectMaster(subjectMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message(msg)
                        .build());
    }

    @PostMapping("/getAllSubjectMasterByFilter")
    public ResponseEntity<?> getAllSubjectMasterByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                         @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = subjectMasterService.getAllSubjectMasterByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(data)
                        .build());
    }

}
