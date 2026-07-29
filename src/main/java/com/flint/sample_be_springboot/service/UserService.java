package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ScreenMasterDTO;
import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;

import com.flint.sample_be_springboot.entity.ScreenMaster;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO getUserById(Long userId);

    UserDTO saveUser(SignUpDTO signUpDTO);

    UserDTO updateUser(UserDTO userDTO);

    String deleteUser(Long userId);

    Map<String, Object> getAllUsersByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

    List<ScreenMasterDTO> getAllScreens();

}
