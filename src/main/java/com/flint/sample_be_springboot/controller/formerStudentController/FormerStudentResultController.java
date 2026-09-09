package com.flint.sample_be_springboot.controller.formerStudentController;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentResultDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.FormerStudentService.FormerStudentResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Map;

@RestController
@RequestMapping("/formerStudentResult")
public class FormerStudentResultController {

    @Autowired
    private FormerStudentResultService formerStudentResultService;

    @PostMapping("/saveFormerStudentResult")
    public ResponseEntity<?> saveFormerStudentResult(@RequestBody FormerStudentResultDTO formerStudentResultDTO) {
        FormerStudentResultDTO resultDTO = formerStudentResultService.saveFormerStudentResult(formerStudentResultDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(resultDTO).build());
    }

    @GetMapping("/getFormerStudentResult/{formerStudentResultId}")
    public ResponseEntity<?> getFormerStudentResult(@PathVariable Long formerStudentResultId) {
        FormerStudentResultDTO resultDTO = formerStudentResultService.getFormerStudentResult(formerStudentResultId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(resultDTO).build());
    }

    @PutMapping("/updateFormerStudentResult")
    public ResponseEntity<?> updateFormerStudentResult(@RequestBody FormerStudentResultDTO formerStudentResultDTO) {
        FormerStudentResultDTO resultDTO = formerStudentResultService.updateFormerStudentResult(formerStudentResultDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(resultDTO).build());
    }

    @DeleteMapping("/deleteFormerStudentResult/{formerStudentResultId}")
    public ResponseEntity<?> deleteFormerStudentResult(@PathVariable Long formerStudentResultId) {
        String studentResult = formerStudentResultService.deleteFormerStudentResult(formerStudentResultId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(studentResult).build());
    }

    @PostMapping("/getAllFormerStudentResultByFilter")
    public ResponseEntity<?> getAllFormerStudentResultByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                               @RequestParam(defaultValue = "true") boolean paginate) {
        java.util.Map<String, Object> map = formerStudentResultService.getAllFormerStudentResultByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(map).build());
    }
}
