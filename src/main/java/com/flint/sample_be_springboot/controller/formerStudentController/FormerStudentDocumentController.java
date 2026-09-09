package com.flint.sample_be_springboot.controller.formerStudentController;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDocumentDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.FormerStudentService.FormerStudentDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/formerStudentDocument")
public class FormerStudentDocumentController {

    @Autowired
    private FormerStudentDocumentService formerStudentDocumentService;

    @PostMapping("/saveFormerStudentDocument")
    public ResponseEntity<?> saveFormerStudentDocument(@RequestBody FormerStudentDocumentDTO formerStudentDocumentDTO) {
        FormerStudentDocumentDTO documentDTO = formerStudentDocumentService.saveFormerStudentDocument(formerStudentDocumentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(documentDTO).build());
    }

    @GetMapping("/getFormerStudentDocument/{formerStudentDocumentId}")
    public ResponseEntity<?> getFormerStudentDocument(@PathVariable Long formerStudentDocumentId) {
        FormerStudentDocumentDTO documentDTO = formerStudentDocumentService.getFormerStudentDocument(formerStudentDocumentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(documentDTO).build());
    }

    @PutMapping("/updateFormerStudentDocument")
    public ResponseEntity<?> updateFormerStudentDocument(@RequestBody FormerStudentDocumentDTO formerStudentDocumentDTO) {
        FormerStudentDocumentDTO documentDTO = formerStudentDocumentService.updateFormerStudentDocument(formerStudentDocumentDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(documentDTO).build());
    }

    @DeleteMapping("/deleteFormerStudentDocument/{formerStudentId}")
    public ResponseEntity<?> deleteFormerStudentDocument(@PathVariable Long formerStudentId) {
        String formerStudentDocument = formerStudentDocumentService.deleteFormerStudentDocument(formerStudentId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(formerStudentDocument).build());
    }

    @PostMapping("/getAllFormerStudentDocumentByFilter")
    public ResponseEntity<?> getAllFormerStudentDocumentByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                                 @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> map = formerStudentDocumentService.getAllFormerStudentDocumentByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(map).build());
    }

}
