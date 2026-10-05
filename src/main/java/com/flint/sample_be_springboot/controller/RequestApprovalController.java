package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.RequestApprovalDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.RequestApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/requestApproval")
public class RequestApprovalController {

    @Autowired
    private RequestApprovalService requestApprovalService;


    @GetMapping("/getRequestApproval/{requestApprovalId}")
    public ResponseEntity<?> getSchoolExpenses(@PathVariable Long requestApprovalId) {
        RequestApprovalDTO requestApprovalDTO = requestApprovalService.getRequestApproval(requestApprovalId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(requestApprovalDTO).build());
    }

    @PostMapping("/saveRequestApproval")
    public ResponseEntity<?> saveRequestApproval(@RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO approvalDTO = requestApprovalService.saveRequestApproval(requestApprovalDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(approvalDTO).build());
    }

    @PutMapping("/updateRequestApproval")
    public ResponseEntity<?> updateRequestApproval(@RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO approvalDTO = requestApprovalService.updateRequestApproval(requestApprovalDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(approvalDTO).build());
    }

    @DeleteMapping("/deleteRequestApproval/{requestApprovalId}")
    public ResponseEntity<?> deleteRequestApproval(@PathVariable Long requestApprovalId) {
        String deleteRequestApproval = requestApprovalService.deleteRequestApproval(requestApprovalId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(deleteRequestApproval).build());
    }

    @PostMapping("/getAllRequestApprovalByFilter")
    public ResponseEntity<?> getAllRequestApprovalByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                           @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> map = requestApprovalService.getAllRequestApprovalByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(map).build());
    }

}
