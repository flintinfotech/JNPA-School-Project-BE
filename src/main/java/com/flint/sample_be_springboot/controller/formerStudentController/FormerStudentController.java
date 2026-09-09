package com.flint.sample_be_springboot.controller.formerStudentController;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.FormerStudentService.FormerStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.Map;

@RestController
@RequestMapping("/formerStudents")
public class FormerStudentController {

    @Autowired
    private FormerStudentService formerStudentService;

    @PostMapping("/saveFormerStudent")
    public ResponseEntity<?> saveFormerStudent(@RequestBody FormerStudentDTO formerStudentDTO) {
        FormerStudentDTO dto = formerStudentService.saveFormerStudent(formerStudentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(dto).build());
    }

    @GetMapping("/getFormerStudent/{formerStudentId}")
    public ResponseEntity<?> getFormerStudent(@PathVariable Long formerStudentId) {
        FormerStudentDTO dto = formerStudentService.getFormerStudent(formerStudentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(dto).build());
    }

    @PutMapping("/updateFormerStudent")
    public ResponseEntity<?> updateFormerStudent(@RequestBody FormerStudentDTO formerStudentDTO) {
        FormerStudentDTO dto = formerStudentService.updateFormerStudent(formerStudentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(dto).build());
    }

    @DeleteMapping("/deleteFormerStudent/{formerStudentId}")
    public ResponseEntity<?> deleteFormerStudent(@PathVariable Long formerStudentId) {
        String formerStudent = formerStudentService.deleteFormerStudent(formerStudentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(formerStudent).build());
    }

    @PostMapping("/getAllFormerStudentByFilter")
    public ResponseEntity<?> getAllFormerStudentByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                         @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> map = formerStudentService.getAllFormerStudentByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }


}
