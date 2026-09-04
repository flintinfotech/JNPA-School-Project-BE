package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.SchoolExpensesDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.SchoolExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/schoolExpenses")
public class SchoolExpensesController {

    @Autowired
    private SchoolExpensesService schoolExpensesService;

    @PostMapping("/saveSchoolExpenses")
    public ResponseEntity<?> saveSchoolExpenses(@RequestBody SchoolExpensesDTO schoolExpensesDTO) {
        SchoolExpensesDTO expensesDTO = schoolExpensesService.saveSchoolExpenses(schoolExpensesDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(expensesDTO).build());
    }

    @GetMapping("/getSchoolExpenses/{schoolExpenseId}")
    public ResponseEntity<?> getSchoolExpenses(@PathVariable Long schoolExpenseId) {
        SchoolExpensesDTO expensesDTO = schoolExpensesService.getSchoolExpenses(schoolExpenseId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(expensesDTO).build());
    }

    @PutMapping("/updateSchoolExpenses")
    public ResponseEntity<?> updateSchoolExpenses(@RequestBody SchoolExpensesDTO schoolExpensesDTO) {
        SchoolExpensesDTO expensesDTO = schoolExpensesService.updateSchoolExpenses(schoolExpensesDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(expensesDTO).build());
    }

    @DeleteMapping("/deleteSchoolExpenses/{schoolExpenseId}")
    public ResponseEntity<?> deleteSchoolExpenses(@PathVariable Long schoolExpenseId) {
        String deleteSchoolExpenses = schoolExpensesService.deleteSchoolExpenses(schoolExpenseId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(deleteSchoolExpenses).build());
    }

    @PostMapping("/getAllSchoolExpensesByFilter")
    public ResponseEntity<?> getAllSchoolExpensesByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                          @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> map = schoolExpensesService.getAllSchoolExpensesByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(map).build());
    }


}
