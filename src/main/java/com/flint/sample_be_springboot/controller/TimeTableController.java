package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.TimeTableDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    @Autowired
    private TimeTableService timeTableService;

    @PostMapping("/saveTimeTable")
    public ResponseEntity<?> saveTimeTable(@RequestBody TimeTableDTO timeTableDTO) {
        TimeTableDTO tableDTO = timeTableService.saveTimeTable(timeTableDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Time table successfully added").data(tableDTO).build());
    }

    @GetMapping("/getTimeTableByTimeTableId/{timeTableId}")
    public ResponseEntity<?> getTimeTableByTimeTableId(@PathVariable Long timeTableId) {
        TimeTableDTO tableDTO = timeTableService.getTimeTableByTimeTableId(timeTableId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(tableDTO).build());
    }

    @PutMapping("/updateTimeTable")
    public ResponseEntity<?> updateTimeTable(@RequestBody TimeTableDTO timeTableDTO) {
        TimeTableDTO data = timeTableService.updateTimeTable(timeTableDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Time table updated successfully").data(data).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteTimeTable/{tableId}")
    public ResponseEntity<?> deleteTimeTable(@PathVariable Long tableId) {
        String msg = timeTableService.deleteTimeTable(tableId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }


    @PostMapping("/getAllTimeTableByFilter")
    public ResponseEntity<?> getALlTimeTableByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                     @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = timeTableService.getAllTimeTableByFilter(filter, pageable, paginate);

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
