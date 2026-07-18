package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ClassMasterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/classMaster")
public class ClassMasterController {

    @Autowired
    private ClassMasterServiceImpl classMasterServiceImpl;

    @GetMapping("/getClassMasterById/{classMasterId}")
    public ResponseEntity<?> getClassMasterById(@PathVariable Long classMasterId) {

        ClassMasterDTO classMasterDTO = classMasterServiceImpl.getClassMasterById(classMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(classMasterDTO)
                        .build());
    }

    @PostMapping("/saveClassMaster")
    public ResponseEntity<?> saveClassMaster(@RequestBody ClassMasterDTO classMasterDTO) {

        ClassMasterDTO data = classMasterServiceImpl.saveClassMaster(classMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Class saved successfully")
                        .data(data)
                        .build());
    }

    @PutMapping("/updateClassMaster")
    public ResponseEntity<?> updateClassMaster(@RequestBody ClassMasterDTO classMasterDTO) {

        ClassMasterDTO data = classMasterServiceImpl.updateClassMaster(classMasterDTO);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Class updated successfully")
                        .data(data)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteClassMaster/{classMasterId}")
    public ResponseEntity<?> deleteClassMaster(@PathVariable Long classMasterId) {

        String msg = classMasterServiceImpl.deleteClassMaster(classMasterId);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message(msg)
                        .build());
    }

    @PostMapping("/getAllClassMasterByFilter")
    public ResponseEntity<?> getAllClassMasterByFilter(@RequestBody Map<String, Object> filter,
                                                       Pageable pageable, boolean paginate) {

        Map<String, Object> data = classMasterServiceImpl.getAllClassMasterByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(
                APIResponse.builder()
                        .success(true)
                        .message("Data fetched successfully")
                        .data(data)
                        .build());
    }

}