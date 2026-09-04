package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.HomeworkDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.HomeworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @GetMapping("/getHomeworkById/{homeworkId}")
    public ResponseEntity<?> getHomeworkById(@PathVariable Long homeworkId) {

        HomeworkDTO homeworkDTO = homeworkService.getHomeworkById(homeworkId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(homeworkDTO).build());
    }

    @PostMapping("/saveHomework")
    public ResponseEntity<?> saveHomework(@RequestBody HomeworkDTO homeworkDTO) {

        HomeworkDTO data = homeworkService.saveHomework(homeworkDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Homework saved successfully").data(data).build());
    }

    @PutMapping("/updateHomework")
    public ResponseEntity<?> updateHomework(@RequestBody HomeworkDTO homeworkDTO) {

        HomeworkDTO data = homeworkService.updateHomework(homeworkDTO);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Homework updated successfully").data(data).build());
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteHomework/{homeworkId}")
    public ResponseEntity<?> deleteHomework(@PathVariable Long homeworkId) {

        String msg = homeworkService.deleteHomework(homeworkId);

        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllHomeworkByFilter")
    public ResponseEntity<?> getAllHomeworkByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                    @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = homeworkService.getAllHomeworkByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }
}