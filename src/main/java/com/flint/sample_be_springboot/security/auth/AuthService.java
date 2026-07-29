package com.flint.sample_be_springboot.security.auth;

import com.flint.sample_be_springboot.dto.LoginRequest;
import com.flint.sample_be_springboot.dto.ScreenMasterDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.UserScreenAccessEntity;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.security.jwt.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    Map<String, Object> login(LoginRequest request) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed");
        }

        UserEntity userEntity = userRepository.findByUserName(request.getUsername()).get();
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        List<ScreenMasterDTO> screens = userEntity.getScreenAccesses()
                .stream()
                .map(UserScreenAccessEntity::getScreen)
                .map(screen -> modelMapper.map(screen, ScreenMasterDTO.class))
                .toList();

        userDTO.setScreens(screens);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails, request.getAcademicWorkYearDTO());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userDTO", userDTO);
        map.put("academicYearDTO", request.getAcademicWorkYearDTO());

        return map;
    }
}
