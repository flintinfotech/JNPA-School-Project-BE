package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.ScreenMasterDTO;
import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get")
    public String getUser() {
        log.info("Entering get...");
        return "User retrieved successfully ";
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(userDTO).build());
    }

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody SignUpDTO signUpDTO) {
        UserDTO data = userService.saveUser(signUpDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("User saved successfully").data(data).build());
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        UserDTO data = userService.updateUser(userDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("User updated successfully").data(data).build());
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        String msg = userService.deleteUser(userId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllUsersByFilter")
    public ResponseEntity<?> getAllUsersByFilter(@RequestBody Map<String, Object> filter, Pageable pageable, boolean paginate) {
        Map<String, Object> data = userService.getAllUsersByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

    @GetMapping("/getAllScreens")
    public ResponseEntity<?> getAllScreens(){
        List<ScreenMasterDTO> screenMasterDTOS = userService.getAllScreens();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(screenMasterDTOS).build());
    }

}

