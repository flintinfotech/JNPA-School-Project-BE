package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.AcademicCalendarDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.AcademicCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/academicCalendarEvents")
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    @GetMapping("/getAcademicCalendarEventById/{academicCalendarId}")
    public ResponseEntity<?> getAcademicCalendarEventById(@PathVariable Long academicCalendarId) {
        AcademicCalendarDTO data = academicCalendarService.getAcademicCalendarEventById(academicCalendarId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record fetched successfully").data(data).build());
    }

    @PostMapping("/saveAcademicCalendarEvent")
    public ResponseEntity<?> saveAcademicCalendarEvent(@RequestBody AcademicCalendarDTO academicCalendarDTO) {
        AcademicCalendarDTO data = academicCalendarService.saveAcademicCalendarEvent(academicCalendarDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record saved successfully").data(data).build());
    }

    @PutMapping("/updateAcademicCalendarEvent")
    public ResponseEntity<?> updateAcademicCalendarEvent(@RequestBody AcademicCalendarDTO academicCalendarDTO) {
        AcademicCalendarDTO data = academicCalendarService.updateAcademicCalendarEvent(academicCalendarDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record updated successfully").data(data).build());
    }

    @DeleteMapping("/deleteAcademicCalendarEvent/{academicCalendarId}")
    public ResponseEntity<?> deleteAcademicCalendarEvent(@PathVariable Long academicCalendarId) {
        String msg = academicCalendarService.deleteAcademicCalendarEvent(academicCalendarId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).data(null).build());
    }

    @PostMapping("/getAllAcademicCalendarEventsByFilter")
    public ResponseEntity<?> getAllAcademicCalendarEventsByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                          @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> data = academicCalendarService.getAllAcademicCalendarEventsByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Record fetched successfully").data(data).build());
    }

}
