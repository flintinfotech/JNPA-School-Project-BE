package com.flint.sample_be_springboot.controller;

import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.service.StaticDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staticData")
@CrossOrigin("*")
public class StaticDataController {

    private static Logger LOGGER = LoggerFactory.getLogger(StaticDataController.class);

    private StaticDataService staticDataService;

    StaticDataController(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @GetMapping("/getAllStaticData")
    public ResponseEntity<?> getAllStaticData() {
        LOGGER.info("Enter into getAllStaticData()");
        Map<String, List<String>> data = staticDataService.getAllStaticData();
        return ResponseEntity.ok(APIResponse.builder().success(true).message("Data fetched successfully").data(data).build());
    }
}
