package com.flint.sample_be_springboot.security.jwt;

import com.flint.sample_be_springboot.dto.AcademicWorkYearDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Since we need a Secret Key type for signing and not String type, we wrap it
    private final SecretKey signingKey =
            Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return signingKey;
    }

    public String generateToken(UserDetails userDetails, AcademicWorkYearDTO academicWorkYearDTO) {

        Map<String, Object> academicYearMap = new HashMap<>();
        academicYearMap.put("startDate", academicWorkYearDTO.getStartDate().toString());
        academicYearMap.put("endDate", academicWorkYearDTO.getEndDate().toString());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                )
                .claim("academicYear", academicYearMap)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + jwtExpiration)
                )
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token)
                .get("role", String.class);
    }

    public AcademicWorkYearDTO extractAcademicYear(String token) {

        Claims claims = extractAllClaims(token);

        Map<String, String> academicYear =
                claims.get("academicYear", Map.class);

        AcademicWorkYearDTO dto = new AcademicWorkYearDTO();
        dto.setStartDate(LocalDate.parse(academicYear.get("startDate")));
        dto.setEndDate(LocalDate.parse(academicYear.get("endDate")));

        return dto;
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    public boolean isTokenValid(String token) {

        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
