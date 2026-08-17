package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.student.ParentDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @GetMapping("/getParentById/{parentId}")
    public ResponseEntity<?> getParentById(@PathVariable Long parentId) {

        ParentDTO parentDTO = parentService.getParentById(parentId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(parentDTO).build());
    }

    @PostMapping("/saveParent")
    public ResponseEntity<?> saveParent(@RequestBody ParentDTO parentDTO) {

        ParentDTO data = parentService.saveParent(parentDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Parent saved successfully").data(data).build());
    }

    @PutMapping("/updateParent")
    public ResponseEntity<?> updateParent(@RequestBody ParentDTO parentDTO) {

        ParentDTO data = parentService.updateParent(parentDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Parent updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteParent/{parentId}")
    public ResponseEntity<?> deleteParent(@PathVariable Long parentId) {

        String msg = parentService.deleteParent(parentId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllParentByFilter")
    public ResponseEntity<?> getAllParentByFilter(
            @RequestBody Map<String, Object> filter,
            Pageable pageable,
            @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = parentService.getAllParentByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }
}
