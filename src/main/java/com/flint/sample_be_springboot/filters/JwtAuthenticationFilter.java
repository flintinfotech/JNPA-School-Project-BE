package com.flint.sample_be_springboot.filters;

import com.flint.sample_be_springboot.security.CustomUserDetailsService;
import com.flint.sample_be_springboot.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No JWT present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String jwtToken = authHeader.substring(7);

            String username = jwtService.extractUsername(jwtToken);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtService.isTokenValid(jwtToken)) {

                    String role = jwtService.extractRole(jwtToken);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(
                                            new SimpleGrantedAuthority(role)
                                    )
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
//                    System.err.println(authentication);

//                    System.out.println("User authenticated: " + username);
                }
            }

        } catch (Exception e) {
            System.out.println("JWT Validation Failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
