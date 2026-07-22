package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.dto.TeacherClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.TeacherClassSubjectAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teacherSubjectAssignment")
public class TeacherClassSubjectAllocationController {

    @Autowired
    private TeacherClassSubjectAllocationService teacherClassSubjectAllocationService;

    @PostMapping("/assignOrUnassignSubjects")
    public ResponseEntity<?> assignOrUnassignSubjects(@RequestBody TeacherClassSubjectAllocationDTO dto) {
        String data = teacherClassSubjectAllocationService.updateTeacherClassSubjectAllocation(dto);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Records saved successfully").data(data).build());
    }

    @GetMapping("/getSubjectsByUserInformationId/{userInformationId}")
    public ResponseEntity<?> getSubjectsByUserInformationId(@PathVariable Long userInformationId) {
        Map<ClassMasterDTO, List<SubjectMasterDTO>> map = teacherClassSubjectAllocationService.getTeacherClassSubjectAllocation(userInformationId);
        if(map.isEmpty()){
            return ResponseEntity.ok(APIResponse.builder().success(true).message("No subjects are assigned to this class").data(map).build());
        }
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(map).build());
    }

}
