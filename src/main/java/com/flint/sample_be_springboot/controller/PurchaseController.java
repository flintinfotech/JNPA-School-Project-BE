package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/savePurchase")
    public ResponseEntity<?> saveStudent(@RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO data = purchaseService.savePurchase(purchaseDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Purchase saved successfully").data(data).build());
    }

    @GetMapping("/getPurchase/{purchaseId}")
    public ResponseEntity<?> getPurchaseByPurchaseId(@PathVariable Long purchaseId) {
        PurchaseDTO data = purchaseService.getPurchaseByPurchaseId(purchaseId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data Found successfully").data(data).build());
    }

    @PutMapping("/updatePurchase")
    public ResponseEntity<?> updatePurchase(@RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO data = purchaseService.updatePurchase(purchaseDTO);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data updated successfully").data(data).build());
    }

    @DeleteMapping("/deletePurchase/{purchaseId}")
    public ResponseEntity<?> deletePurchase(@PathVariable Long purchaseId) {
        String data = purchaseService.deletePurchase(purchaseId);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data deleted successfully").data(data).build());
    }

    @PostMapping("/getAllPurchaseByFilter")
    public ResponseEntity<?> getAllPurchaseByFilter(@RequestBody Map<String, Object> filter, Pageable pageable,
                                                    @RequestParam(defaultValue = "true") boolean paginate) {

        Map<String, Object> data = purchaseService.getAllPurchaseByFilter(filter, pageable, paginate);
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }


}
