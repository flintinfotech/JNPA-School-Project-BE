package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ScreenMasterDTO;
import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.entity.*;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.enums.Role;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.EmployeeDetailsRepository;
import com.flint.sample_be_springboot.repository.ScreenMasterRepository;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.repository.UserScreenAccessRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import com.flint.sample_be_springboot.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ScreenMasterRepository screenRepository;

    @Autowired
    UserScreenAccessRepository userScreenAccessRepository;

    @Autowired
    EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    StudentRepository studentRepository;

    public UserDTO getUserById(Long userId) {
        log.info("Enter into getUserById");

        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        UserDTO userDTO = modelMapper.map(entity, UserDTO.class);
        List<ScreenMasterDTO> screens = entity.getScreenAccesses()
                .stream()
                .map(UserScreenAccessEntity::getScreen)
                .map(screen -> modelMapper.map(screen, ScreenMasterDTO.class))
                .toList();

        userDTO.setScreens(screens);

        log.info("Enter into getUserById");
        return userDTO;
    }

    public UserDTO saveUser(SignUpDTO signUpDTO) {
        log.info("Enter into saveUser");

        UserDTO savedUser;

        if(!Role.STUDENT.equals(signUpDTO.getRole())) {
            Optional<UserEntity> existingUserEntity = userRepository.findByUserName(signUpDTO.getUserName());
            if (existingUserEntity.isPresent()) {
                throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
            }

            EmployeeDetailsEntity employeeDetails = employeeDetailsRepository.findById(signUpDTO.getEmployeeDetailsId()).get();
            UserEntity userEntity = modelMapper.map(signUpDTO, UserEntity.class);
            userEntity.setEmployeeDetails(employeeDetails);
            userEntity.setAuditDetails(addAuditDetails(userEntity.getAuditDetails()));

            String password = PasswordGenerator.generatePassword(signUpDTO.getFirstName());
            userEntity.setDecryptedPassword(password);

            userEntity.setPassword(passwordEncoder.encode(password)); // encoded password

            UserEntity savedUserEntity = userRepository.save(userEntity);

            savedUser = modelMapper.map(savedUserEntity, UserDTO.class);
            savedUser.setPassword(password);

            // for employee users set profile as a default screen
            ScreenMaster screenMaster = screenRepository.findById(14L).get();
            UserScreenAccessEntity access = new UserScreenAccessEntity();
            access.setUser(savedUserEntity);
            access.setScreen(screenMaster);

            userScreenAccessRepository.save(access);

            savedUser = modelMapper.map(savedUserEntity, UserDTO.class);
            savedUser.setPassword(password);

            savedUser.setScreens(
                    List.of(
                            modelMapper.map(screenMaster, ScreenMasterDTO.class)
                    ));
        } else {

            Optional<UserEntity> existingUserEntity = userRepository.findByMobileNo(signUpDTO.getMobileNo());
            if (existingUserEntity.isPresent()) {
                throw new CustomException("Mobile no. is already exist", HttpStatus.CONFLICT);
            }

            StudentEntity studentEntity = studentRepository.findById(signUpDTO.getStudentId()).get();
            UserEntity userEntity = modelMapper.map(signUpDTO, UserEntity.class);
            userEntity.setStudentEntity(studentEntity);
            userEntity.setAuditDetails(addAuditDetails(userEntity.getAuditDetails()));

            String password = PasswordGenerator.generatePassword(signUpDTO.getFirstName(), signUpDTO.getDOB());
            userEntity.setDecryptedPassword(password);

            userEntity.setPassword(passwordEncoder.encode(password)); // encoded password

            UserEntity savedUserEntity = userRepository.save(userEntity);

            // for student users set student profile as a default screen
            ScreenMaster screenMaster = screenRepository.findById(15L).get();
            UserScreenAccessEntity access = new UserScreenAccessEntity();
            access.setUser(savedUserEntity);
            access.setScreen(screenMaster);

            userScreenAccessRepository.save(access);

            savedUser = modelMapper.map(savedUserEntity, UserDTO.class);
            savedUser.setPassword(password);

            savedUser.setScreens(
                    List.of(
                            modelMapper.map(screenMaster, ScreenMasterDTO.class)
                    ));
        }

        log.info("Exit from saveUser");
        return savedUser;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Enter into updateUser");

        UserEntity existingEntity = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String encryptedPass = existingEntity.getPassword();
        String decryptedPass = existingEntity.getDecryptedPassword();

        Optional<UserEntity> existingUserEntity = userRepository.findByUserNameAndUserIdNot(userDTO.getUserName(), userDTO.getUserId());
        if (existingUserEntity.isPresent()) {
            throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
        }

        AuditDetails auditDetails = existingEntity.getAuditDetails();

        modelMapper.map(userDTO, existingEntity);

        if (!Role.STUDENT.equals(existingEntity.getRole()) && !existingEntity.equals(userDTO.getRole())) {
            existingEntity.getEmployeeDetails().setRole(userDTO.getRole());
        }

        // In the future, Password reset functionality is required here
        //---------------------------------------------------------
        existingEntity.setPassword(encryptedPass);
        existingEntity.setDecryptedPassword(decryptedPass);
        //---------------------------------------------------------
        existingEntity.setAuditDetails(addAuditDetails(auditDetails));

        // Existing Screen Mappings
        Map<Long, UserScreenAccessEntity> existingScreenMap =
                existingEntity.getScreenAccesses()
                        .stream()
                        .collect(Collectors.toMap(
                                access -> access.getScreen().getScreenId(),
                                Function.identity()));

        // Requested Screen Ids
        Set<Long> requestScreenIds =
                userDTO.getScreens() == null
                        ? Collections.emptySet()
                        : userDTO.getScreens()
                        .stream()
                        .map(ScreenMasterDTO::getScreenId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        // Remove Unselected Screens
        existingEntity.getScreenAccesses().removeIf(access ->
                !requestScreenIds.contains(access.getScreen().getScreenId()));

        // Add Newly Selected Screens
        if (userDTO.getScreens() != null) {

            for (ScreenMasterDTO screenDTO : userDTO.getScreens()) {

                if (screenDTO.getScreenId() == null) {
                    continue;
                }

                if (!existingScreenMap.containsKey(screenDTO.getScreenId())) {

                    ScreenMaster screen = screenRepository
                            .findById(screenDTO.getScreenId())
                            .orElseThrow(() ->
                                    new CustomException("Screen not found",
                                            HttpStatus.NOT_FOUND));

                    UserScreenAccessEntity access = UserScreenAccessEntity.builder()
                            .user(existingEntity)
                            .screen(screen)
                            .build();

                    existingEntity.getScreenAccesses().add(access);
                }
            }
        }

        UserEntity savedEntity = userRepository.save(existingEntity);

        List<ScreenMasterDTO> screens = savedEntity.getScreenAccesses()
                .stream()
                .map(UserScreenAccessEntity::getScreen)
                .map(screen -> modelMapper.map(screen, ScreenMasterDTO.class))
                .toList();

        UserDTO savedDTO = modelMapper.map(savedEntity, UserDTO.class);
        savedDTO.setScreens(screens);

        log.info("Exit from updateUser");
        return savedDTO;
    }

    public String deleteUser(Long userId) {
        log.info("Enter into deleteUser");

        UserEntity existingEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        userRepository.delete(existingEntity);

        log.info("Exit from deleteUser");
        return "User deleted successfully";
    }

    public Map<String, Object> getAllUsersByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllUsersByFilter");

        Page<UserEntity> userEntityPage;
        List<UserEntity> userEntityList;
        Long totalElement = 0L;

        CustomQuerySpecification<UserEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            userEntityPage = userRepository.findAll(customQuerySpecification, pageable);
            userEntityList = userEntityPage.getContent();
            totalElement = userEntityPage.getTotalElements();
        } else {
            userEntityList = userRepository.findAll(customQuerySpecification);
            totalElement = (long) userEntityList.size();
        }

        List<UserDTO> userDTOS = userEntityList.stream()
                .filter(user -> !"Superadmin".equalsIgnoreCase(user.getUserName())) // main superadmin
                .map(u -> {
                    UserDTO dto = modelMapper.map(u, UserDTO.class);

                    List<ScreenMasterDTO> screens = u.getScreenAccesses()
                            .stream()
                            .map(UserScreenAccessEntity::getScreen)
                            .map(screen -> modelMapper.map(screen, ScreenMasterDTO.class))
                            .toList();

                    dto.setScreens(screens);

                    return dto;
                }).toList();

        log.info("Exit from getAllUsersByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", userDTOS);
        result.put("total", totalElement);
        return result;
    }

    public List<ScreenMasterDTO> getAllScreens() {
        log.info("Exit from getAllScreens");

        List<ScreenMaster> screenMasters = screenRepository.findAll();

        List<ScreenMasterDTO> screenMasterDTOS = screenMasters.stream()
                .map(s -> modelMapper.map(s, ScreenMasterDTO.class))
                .collect(Collectors.toList());

        log.info("Exit from getAllScreens");

        return screenMasterDTOS;
    }

}
