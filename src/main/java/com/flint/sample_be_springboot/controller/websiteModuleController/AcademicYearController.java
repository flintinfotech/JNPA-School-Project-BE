package com.flint.sample_be_springboot.controller.websiteModuleController;

import com.flint.sample_be_springboot.dto.websiteModuleDTOS.classRoom.AcademicYearDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.websiteModuleService.AcademicYearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/academicYear")
public class AcademicYearController {

    @Autowired
    AcademicYearService academicYearService;

    @GetMapping("/getAcademicYearById/{academicYearId}")
    public ResponseEntity<?> getAcademicYearById(@PathVariable Long academicYearId) {
        AcademicYearDTO academicYearDTO = academicYearService.getAcademicYearById(academicYearId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(academicYearDTO).build());
    }

    @PostMapping("/saveAcademicYear")
    public ResponseEntity<?> saveAcademicYear(@RequestBody AcademicYearDTO academicYearDTO) {
        AcademicYearDTO data = academicYearService.saveAcademicYear(academicYearDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Academic Year saved successfully").data(data).build());
    }

    @PutMapping("/updateAcademicYear")
    public ResponseEntity<?> updateAcademicYear(@RequestBody AcademicYearDTO academicYearDTO) {
        AcademicYearDTO data = academicYearService.updateAcademicYear(academicYearDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Academic Year updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAcademicYear/{academicYearId}")
    public ResponseEntity<?> deleteAcademicYear(@PathVariable Long academicYearId) {
        String msg = academicYearService.deleteAcademicYear(academicYearId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllAcademicYearsByFilter")
    public ResponseEntity<?> getAllAcademicYearsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                         @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = academicYearService.getAllAcademicYearsByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }

    @GetMapping("/getCurrentAcademicYear")
    public ResponseEntity<?> getCurrentAcademicYear() {
        AcademicYearDTO academicYearDTO = academicYearService.getCurrentAcademicYear();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Current Academic Year fetched successfully").data(academicYearDTO).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/setCurrentAcademicYear/{academicYearId}")
    public ResponseEntity<?> setCurrentAcademicYear(@PathVariable Long academicYearId) {
        AcademicYearDTO data = academicYearService.setCurrentAcademicYear(academicYearId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Current Academic Year updated successfully").data(data).build());
    }

}
