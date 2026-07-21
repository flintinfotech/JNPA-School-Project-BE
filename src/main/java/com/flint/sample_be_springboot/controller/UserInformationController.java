package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.UserInformationDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.UserInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userInformation")
public class UserInformationController {

    @Autowired
    private UserInformationService userInformationService;

    @GetMapping("/getUserInformationById/{userInformationId}")
    public ResponseEntity<?> getUserInformationById(@PathVariable Long userInformationId) {

        UserInformationDTO userInformationDTO = userInformationService.getUserInformationById(userInformationId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(userInformationDTO).build());
    }

    @PostMapping("/saveUserInformation")
    public ResponseEntity<?> saveUserInformation(@RequestBody UserInformationDTO userInformationDTO) {

        UserInformationDTO data = userInformationService.saveUserInformation(userInformationDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("User information saved successfully").data(data).build());
    }

    @PutMapping("/updateUserInformation")
    public ResponseEntity<?> updateUserInformation(@RequestBody UserInformationDTO userInformationDTO) {

        UserInformationDTO data = userInformationService.updateUserInformation(userInformationDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("User information updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUserInformation/{userInformationId}")
    public ResponseEntity<?> deleteUserInformation(@PathVariable Long userInformationId) {

        String msg = userInformationService.deleteUserInformation(userInformationId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllUserInformationByFilter")
    public ResponseEntity<?> getAllUserInformationByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                           @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = userInformationService.getAllUserInformationByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}