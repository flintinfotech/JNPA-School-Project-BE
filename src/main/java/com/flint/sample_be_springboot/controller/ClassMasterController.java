package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.dto.ClassMasterSearchDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ClassMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/classMaster")
public class ClassMasterController {

    @Autowired
    private ClassMasterService classMasterService;

    @GetMapping("/getClassMasterById/{classMasterId}")
    public ResponseEntity<?> getClassMasterById(@PathVariable Long classMasterId) {

        ClassMasterDTO classMasterDTO = classMasterService.getClassMasterById(classMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(classMasterDTO)
                        .build());
    }

    @PostMapping("/saveClassMaster")
    public ResponseEntity<?> saveClassMaster(@RequestBody ClassMasterDTO classMasterDTO) {

        ClassMasterDTO data = classMasterService.saveClassMaster(classMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Class saved successfully")
                        .data(data)
                        .build());
    }

    @PutMapping("/updateClassMaster")
    public ResponseEntity<?> updateClassMaster(@RequestBody ClassMasterDTO classMasterDTO) {

        ClassMasterDTO data = classMasterService.updateClassMaster(classMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Class updated successfully")
                        .data(data)
                        .build());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteClassMaster/{classMasterId}")
    public ResponseEntity<?> deleteClassMaster(@PathVariable Long classMasterId) {

        String msg = classMasterService.deleteClassMaster(classMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message(msg)
                        .build());
    }

    @PostMapping("/getAllClassMasterByFilter")
    public ResponseEntity<?> getAllClassMasterByFilter(@RequestBody Map<String, Object> filter,
                                                       Pageable pageable, boolean paginate) {

        Map<String, Object> data = classMasterService.getAllClassMasterByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(data)
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClasses(@RequestParam String keyword) {

        List<ClassMasterSearchDTO> data = classMasterService.searchClasses(keyword);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}