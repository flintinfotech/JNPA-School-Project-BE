package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDTO getUserById(Long userId){
        log.info("Enter into getUserById");

            UserEntity entity = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            UserDTO userDTO = modelMapper.map(entity, UserDTO.class);

        log.info("Enter into getUserById");
        return userDTO;
    }

    public UserDTO saveUser(SignUpDTO signUpDTO){
        log.info("Enter into saveUser");

        Optional<UserEntity> existingUserEntity = userRepository.findByUserName(signUpDTO.getUserName());
        if (existingUserEntity.isPresent()) {
            throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
        }

        UserEntity userEntity = modelMapper.map(signUpDTO, UserEntity.class);

        userEntity.setPassword(passwordEncoder.encode(signUpDTO.getPassword())); // encoded password

        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDTO savedUser = modelMapper.map(savedUserEntity, UserDTO.class);

        log.info("Exit from saveUser");
        return savedUser;
    }

    public UserDTO updateUser(UserDTO userDTO){
        log.info("Enter into updateUser");

        UserEntity existingEntity = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        Optional<UserEntity> existingUserEntity = userRepository.findByUserNameAndUserIdNot(userDTO.getUserName(), userDTO.getUserId());
        if(existingUserEntity.isPresent()){
            throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
        }

        modelMapper.map(userDTO, existingEntity);

        UserEntity savedEntity = userRepository.save(existingEntity);
        UserDTO savedDTO = modelMapper.map(savedEntity, UserDTO.class);

        log.info("Exit from updateUser");
        return savedDTO;
    }

    public String deleteUser(Long userId){
        log.info("Enter into deleteUser");

        UserEntity existingEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        userRepository.delete(existingEntity);

        log.info("Exit from deleteUser");
        return "User deleted successfully";
    }

    public Map<String, Object> getAllUsersByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate){
        log.info("Enter into getAllUsersByFilter");

        Page<UserEntity> userEntityPage;
        List<UserEntity> userEntityList;
        Long totalElement = 0l;

        CustomQuerySpecification<UserEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if(paginate){
            userEntityPage = userRepository.findAll(customQuerySpecification, pageable);
            userEntityList = userEntityPage.getContent();
            totalElement = userEntityPage.getTotalElements();
        }else{
            userEntityList = userRepository.findAll(customQuerySpecification);
            totalElement = (long) userEntityList.size();
        }

        List<UserDTO> userDTOS = userEntityList.stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .toList();

        log.info("Exit from getAllUsersByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", userDTOS);
        result.put("total", totalElement);
        return result;
    }

}
