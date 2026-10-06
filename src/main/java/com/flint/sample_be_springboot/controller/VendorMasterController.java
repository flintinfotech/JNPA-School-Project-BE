package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.VendorMasterDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.VendorMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vendorMaster")
public class VendorMasterController {

    @Autowired
    private VendorMasterService vendorMasterService;

    @PostMapping("/saveVendorMaster")
    public ResponseEntity<?> saveVendorMaster(@RequestBody VendorMasterDTO vendorMasterDTO) {
        VendorMasterDTO vendorMasterDTO1 = vendorMasterService.saveVendorMaster(vendorMasterDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data saved successfully").data(vendorMasterDTO1).build());
    }

    @GetMapping("/getVendorMaster/{vendorMasterId}")
    public ResponseEntity<?> getVendorMaster(@PathVariable Long vendorMasterId) {
        VendorMasterDTO vendorMasterDTO1 = vendorMasterService.getVendorMaster(vendorMasterId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(vendorMasterDTO1).build());
    }

    @PutMapping("/updateVendorMaster")
    public ResponseEntity<?> updateVendorMaster(@RequestBody VendorMasterDTO vendorMasterDTO) {
        VendorMasterDTO vendorMasterDTO1 = vendorMasterService.updateVendorMaster(vendorMasterDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(vendorMasterDTO1).build());
    }

    @DeleteMapping("/deleteVendorMaster/{vendorMasterId}")
    public ResponseEntity<?> deleteVendorMaster(@PathVariable Long vendorMasterId) {
        String msg = vendorMasterService.deleteVendorMaster(vendorMasterId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message(msg).build());
    }

    @PostMapping("/getAllVendorMasterByFilter")
    public ResponseEntity<?> saveVendorMaster(@RequestBody Map<String, Object> filter, Pageable pageable,
                                              @RequestParam(defaultValue = "true") boolean paginate) {
        Map<String, Object> data = vendorMasterService.getAllVendorMasterByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data found successfully").data(data).build());
    }
}
