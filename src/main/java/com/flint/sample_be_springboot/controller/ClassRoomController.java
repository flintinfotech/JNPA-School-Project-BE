package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.classRoom.ClassRoomDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ClassRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/classRoom")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/getClassRoomById/{classRoomId}")
    public ResponseEntity<?> getClassRoomById(@PathVariable Long classRoomId) {
        ClassRoomDTO classRoomDTO = classRoomService.getClassRoomById(classRoomId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(classRoomDTO).build());
    }

    @PostMapping("/saveClassRoom")
    public ResponseEntity<?> saveClassRoom(@RequestBody ClassRoomDTO classRoomDTO) {
        ClassRoomDTO data = classRoomService.saveClassRoom(classRoomDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Class Room saved successfully").data(data).build());
    }

    @PutMapping("/updateClassRoom")
    public ResponseEntity<?> updateClassRoom(@RequestBody ClassRoomDTO classRoomDTO) {
        ClassRoomDTO data = classRoomService.updateClassRoom(classRoomDTO);
        return ResponseEntity.ok(APIResponse.builder()
                .success(true).message("Class Room updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteClassRoom/{classRoomId}")
    public ResponseEntity<?> deleteClassRoom(@PathVariable Long classRoomId) {
        String msg = classRoomService.deleteClassRoom(classRoomId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllClassRoomsByFilter")
    public ResponseEntity<?> getAllClassRoomsByFilter(@RequestBody Map<String, Object> filter,Pageable pageable,
                                                      @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> data = classRoomService.getAllClassRoomsByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

}
