package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.ClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.dto.student.StudentDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.ClassSubjectAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/subjectAssignment")
public class ClassSubjectAllocationController {

    @Autowired
    private ClassSubjectAllocationService classSubjectAllocationService;

    @PostMapping("/assignOrUnassignSubjects")
    public ResponseEntity<?> assignSubjects(@RequestBody ClassSubjectAllocationDTO dto) {
        String data = classSubjectAllocationService.updateClassSubjects(dto);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Records saved successfully").data(data).build());
    }

    @GetMapping("/getSubjectsByClassId/{classMasterId}")
    public ResponseEntity<?> getSubjectsByClassId(@PathVariable Long classMasterId) {
        List<SubjectMasterDTO> subjectMasterDTOS = classSubjectAllocationService.getSubjectsByClass(classMasterId);
        if(subjectMasterDTOS.isEmpty()){
            return ResponseEntity.ok(APIResponse.builder().success(true).message("No subjects are assigned to this class").data(subjectMasterDTOS).build());
        }
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(subjectMasterDTOS).build());
    }
}
