package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.admission.AdmissionDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.AdmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admission")
@Slf4j
public class AdmissionController {

    @Autowired
    private AdmissionService admissionService;

    @GetMapping("/getAdmissionById/{admissionId}")
    public ResponseEntity<?> getAdmissionById(@PathVariable Long admissionId) {
        AdmissionDTO admissionDTO = admissionService.getAdmissionById(admissionId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(admissionDTO).build());
    }

    @PostMapping("/saveAdmission")
    public ResponseEntity<?> saveAdmission(@RequestBody AdmissionDTO admissionDTO) {
        AdmissionDTO data = admissionService.saveAdmission(admissionDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Admission saved successfully").data(data).build());
    }

    @PutMapping("/updateAdmission")
    public ResponseEntity<?> updateAdmission(@RequestBody AdmissionDTO admissionDTO) {
        AdmissionDTO data = admissionService.updateAdmission(admissionDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Admission updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAdmission/{admissionId}")
    public ResponseEntity<?> deleteAdmission(@PathVariable Long admissionId) {
        String msg = admissionService.deleteAdmission(admissionId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllAdmissionsByFilter")
    public ResponseEntity<?> getAllAdmissionsByFilter(@RequestBody Map<String, Object> filter,Pageable pageable,
                                                 @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = admissionService.getAllAdmissionByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}
