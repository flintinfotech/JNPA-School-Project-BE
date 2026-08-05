package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.AdmissionInquiryDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.AdmissionInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/inquiry")
public class AdmissionInquiryController {

    @Autowired
    private AdmissionInquiryService admissionInquiryService;


    @PostMapping("/saveAdmissionInquiry")
    public ResponseEntity<?> saveAdmissionInquiry(@RequestBody AdmissionInquiryDTO admissionInquiryDTO) {
        AdmissionInquiryDTO inquiryDTO = admissionInquiryService.saveAdmissionInquiry(admissionInquiryDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Form submitted successfully").data(inquiryDTO).build());
    }

    @PutMapping("/updateAdmissionInquiryById")
    public ResponseEntity<?> updateAdmissionInquiryById(@RequestBody AdmissionInquiryDTO admissionInquiryDTO,
                                                        @RequestParam Long id) {
        AdmissionInquiryDTO inquiryDTO = admissionInquiryService.updateAdmissionInquiryById(id, admissionInquiryDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Form updated successfully").data(inquiryDTO).build());
    }

    @DeleteMapping("/deleteAdmissionInquiryById")
    public ResponseEntity<?> deleteAdmissionInquiryById(@RequestParam Long id) {
        AdmissionInquiryDTO inquiryDTO = admissionInquiryService.deleteAdmissionInquiryById(id);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Form deleted successfully").data(inquiryDTO).build());
    }


    @PostMapping("/getAllAdmissionInquiryByFilter")
    public ResponseEntity<?> getAllAdmissionInquiryByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                            @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = admissionInquiryService.getAllAdmissionInquiryByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
