package com.flint.sample_be_springboot.security.auth;

import com.flint.sample_be_springboot.dto.LoginRequest;
import com.flint.sample_be_springboot.response.APIResponse;
import com.flint.sample_be_springboot.security.CustomUserDetailsService;
import com.flint.sample_be_springboot.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

}
