package com.flint.sample_be_springboot.security.auth;

import com.flint.sample_be_springboot.dto.AcademicWorkYearDTO;
import com.flint.sample_be_springboot.dto.LoginRequest;
import com.flint.sample_be_springboot.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(APIResponse.builder().success(true).message("Login successful").data(authService.login(request)).build());

    }

    @GetMapping("/getLastFiveAcademicYears")
    public List<AcademicWorkYearDTO> getLastFiveAcademicYears() {

        LocalDate today = LocalDate.now();
        List<AcademicWorkYearDTO> years = new ArrayList<>();

        int year = today.getYear();

        LocalDate currentStart;
        LocalDate currentEnd;

        // If today is on or after 15 June, current academic year starts this year
        if (!today.isBefore(LocalDate.of(year, 6, 15))) {
            currentStart = LocalDate.of(year, 6, 15);
            currentEnd = LocalDate.of(year + 1, 4, 30);
        } else {
            // Otherwise current academic year started last year
            currentStart = LocalDate.of(year - 1, 6, 15);
            currentEnd = LocalDate.of(year, 4, 30);
        }

        for (int i = 0; i < 5; i++) {
            years.add(new AcademicWorkYearDTO(
                    currentStart.minusYears(i),
                    currentEnd.minusYears(i)
            ));
        }

        return years;
    }

}
