package com.flint.sample_be_springboot.config;


import com.flint.sample_be_springboot.dto.AcademicWorkYearDTO;
import com.flint.sample_be_springboot.security.jwt.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class UserContext {

    private String username;

    private AcademicWorkYearDTO academicWorkYearDTO;

    @Autowired
    JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @PostConstruct
    private void init() {
        if (request != null && jwtService != null) {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                this.username = jwtService.extractUsername(token);
                this.academicWorkYearDTO = jwtService.extractAcademicYear(token);
            }
        } else {
            throw new RuntimeException("HttpServletRequest or JwtToken is not properly injected");
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public AcademicWorkYearDTO getAcademicWorkYear() {
        return academicWorkYearDTO;
    }

}
