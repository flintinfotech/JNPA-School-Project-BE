package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeDetailsDTO;
import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.dto.UserDocumentDTO;
import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.UserDocumentEntity;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.EmployeeDetailsRepository;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import com.flint.sample_be_springboot.util.GenerateCodes;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeDetailsServiceImpl extends BaseService implements EmployeeDetailsService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> saveEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        log.info("Enter into saveUserInformation");

        if (employeeDetailsDTO == null) {
            throw new CustomException("User information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        Optional<EmployeeDetailsEntity> existingUserEntity = employeeDetailsRepository.findByUserName(employeeDetailsDTO.getUserName());
        if (existingUserEntity.isPresent()) {
            throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
        }

        Optional<EmployeeDetailsEntity> existingUserEntity1 = employeeDetailsRepository.findByEmployeeCode(employeeDetailsDTO.getEmployeeCode());
        if (existingUserEntity1.isPresent()) {
            throw new CustomException("Employee code is already exist", HttpStatus.CONFLICT);
        }

        EmployeeDetailsEntity employeeDetailsEntity =
                modelMapper.map(employeeDetailsDTO, EmployeeDetailsEntity.class);

        String lastEmployeeCode = employeeDetailsRepository.findLastEmployeeCode();
        String latestEmployeeCode = GenerateCodes.generateEmployeeCode(lastEmployeeCode);
        employeeDetailsEntity.setEmployeeCode(latestEmployeeCode);

        employeeDetailsEntity.setAuditDetails(addAuditDetails(employeeDetailsEntity.getAuditDetails()));

        // Set User Entity
//        if (employeeDetailsDTO.getUserId() != null) {
//
//            UserEntity userEntity = userRepository.findById(employeeDetailsDTO.getUserId())
//                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
//
//            employeeDetailsEntity.setUserEntity(userEntity);
//        }

        // Set User Document entities
        List<UserDocumentEntity> userDocumentEntities = new ArrayList<>();

        if (employeeDetailsDTO.getUserDocumentDTOS() != null &&
                !employeeDetailsDTO.getUserDocumentDTOS().isEmpty()) {

            for (UserDocumentDTO userDocumentDTO : employeeDetailsDTO.getUserDocumentDTOS()) {

                UserDocumentEntity userDocumentEntity =
                        modelMapper.map(userDocumentDTO, UserDocumentEntity.class);

                if (userDocumentDTO.getDocument() != null) {
                    userDocumentEntity.setDocument(
                            Base64.getDecoder().decode(userDocumentDTO.getDocument()));
                }

                userDocumentEntity.setEmployeeDetailsEntity(employeeDetailsEntity);
                userDocumentEntity.setAuditDetails(addAuditDetails(userDocumentEntity.getAuditDetails()));

                userDocumentEntities.add(userDocumentEntity);
            }
        }

        employeeDetailsEntity.setUserDocumentEntities(userDocumentEntities);

        // Save
        EmployeeDetailsEntity savedEntity = employeeDetailsRepository.save(employeeDetailsEntity);

        SignUpDTO signUpDTO = modelMapper.map(employeeDetailsDTO, SignUpDTO.class);
        signUpDTO.setEmployeeDetailsId(savedEntity.getEmployeeDetailsId());

        UserDTO userDTO = userService.saveUser(signUpDTO);

        // Return DTO
        EmployeeDetailsDTO savedDTO = modelMapper.map(savedEntity, EmployeeDetailsDTO.class);

//        if (savedEntity.getUserEntity() != null) {
//            savedDTO.setUserId(savedEntity.getUserEntity().getUserId());
//        }

        // Set User Document DTOs
        List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

        if (savedEntity.getUserDocumentEntities() != null &&
                !savedEntity.getUserDocumentEntities().isEmpty()) {

            for (UserDocumentEntity userDocumentEntity :
                    savedEntity.getUserDocumentEntities()) {

                UserDocumentDTO userDocumentDTO =
                        modelMapper.map(userDocumentEntity, UserDocumentDTO.class);

                userDocumentDTOS.add(userDocumentDTO);
            }
        }

        savedDTO.setUserDocumentDTOS(userDocumentDTOS);

        log.info("Exit from saveUserInformation");

        Map<String, Object> map = new HashMap<>();
        map.put("Employee details", savedDTO);
        map.put("user details", userDTO);

        return map;
    }

    @Override
    public EmployeeDetailsDTO updateEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        log.info("Enter into updateUserInformation");

        if (employeeDetailsDTO == null) {
            throw new CustomException("User information cannot be null",
                    HttpStatus.PRECONDITION_FAILED);
        }

        EmployeeDetailsEntity existingEntity =
                employeeDetailsRepository.findById(employeeDetailsDTO.getEmployeeDetailsId())
                        .orElseThrow(() ->
                                new CustomException("User information not found",
                                        HttpStatus.NOT_FOUND));

        Optional<EmployeeDetailsEntity> existingUserEntity = employeeDetailsRepository.findByUserNameAndEmployeeDetailsIdNot
                (employeeDetailsDTO.getUserName(), employeeDetailsDTO.getEmployeeDetailsId());
        if (existingUserEntity.isPresent()) {
            throw new CustomException("Username is already exist", HttpStatus.CONFLICT);
        }

        Optional<EmployeeDetailsEntity> existingUserEntity1 = employeeDetailsRepository.findByEmployeeCodeAndEmployeeDetailsIdNot
                (employeeDetailsDTO.getEmployeeCode(), employeeDetailsDTO.getEmployeeDetailsId());
        if (existingUserEntity1.isPresent()) {
            throw new CustomException("Employee code is already exist", HttpStatus.CONFLICT);
        }

        // Update User reference
//        if (employeeDetailsDTO.getUserId() != null) {
//
//            UserEntity userEntity = userRepository.findById(employeeDetailsDTO.getUserId())
//                    .orElseThrow(() ->
//                            new CustomException("User not found", HttpStatus.NOT_FOUND));
//
//            existingEntity.setUserEntity(userEntity);
//        }

        // Update basic fields
        existingEntity.setEmployeeCode(employeeDetailsDTO.getEmployeeCode());
        existingEntity.setFirstName(employeeDetailsDTO.getFirstName());
        existingEntity.setMiddleName(employeeDetailsDTO.getMiddleName());
        existingEntity.setLastName(employeeDetailsDTO.getLastName());
        existingEntity.setGender(employeeDetailsDTO.getGender());
        existingEntity.setDateOfBirth(employeeDetailsDTO.getDateOfBirth());
        existingEntity.setAddress(employeeDetailsDTO.getAddress());
        existingEntity.setQualification(employeeDetailsDTO.getQualification());
        existingEntity.setSpecialization(employeeDetailsDTO.getSpecialization());
        existingEntity.setExperience(employeeDetailsDTO.getExperience());
        existingEntity.setDesignation(employeeDetailsDTO.getDesignation());
        existingEntity.setJoiningDate(employeeDetailsDTO.getJoiningDate());
        existingEntity.setBloodGroup(employeeDetailsDTO.getBloodGroup());
        existingEntity.setEmail(employeeDetailsDTO.getEmail());
        existingEntity.setUserName(employeeDetailsDTO.getUserName());
        existingEntity.setMobileNo(employeeDetailsDTO.getMobileNo());
        existingEntity.setRole(employeeDetailsDTO.getRole());

        if(!existingEntity.equals(employeeDetailsDTO.getRole())){
            existingEntity.getUserEntity().setRole(employeeDetailsDTO.getRole());
        }

        existingEntity.setAuditDetails(
                addAuditDetails(existingEntity.getAuditDetails()));

        // Remove deleted documents
        Set<Long> requestDocumentIds = employeeDetailsDTO.getUserDocumentDTOS().stream()
                .map(UserDocumentDTO::getUserDocumentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getUserDocumentEntities().removeIf(document ->
                document.getUserDocumentId() != null &&
                        !requestDocumentIds.contains(document.getUserDocumentId()));

        // Existing document map
        Map<Long, UserDocumentEntity> existingDocuments =
                existingEntity.getUserDocumentEntities().stream()
                        .collect(Collectors.toMap(
                                UserDocumentEntity::getUserDocumentId,
                                Function.identity()));

        if (employeeDetailsDTO.getUserDocumentDTOS() != null) {

            for (UserDocumentDTO documentDTO : employeeDetailsDTO.getUserDocumentDTOS()) {

                UserDocumentEntity documentEntity;

                // Update existing
                if (documentDTO.getUserDocumentId() != null &&
                        existingDocuments.containsKey(documentDTO.getUserDocumentId())) {

                    documentEntity = existingDocuments.get(documentDTO.getUserDocumentId());

                    if (documentDTO.getDocument() != null) {
                        documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                    }
                    documentEntity.setAuditDetails(addAuditDetails(documentEntity.getAuditDetails()));
                }

                // Add new
                else {

                    documentEntity = new UserDocumentEntity();

                    documentEntity.setEmployeeDetailsEntity(existingEntity);

                    if (documentDTO.getDocument() != null) {
                        documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                    }

                    documentEntity.setAuditDetails(addAuditDetails(documentEntity.getAuditDetails()));

                    existingEntity.getUserDocumentEntities().add(documentEntity);
                }

                documentEntity.setDocumentName(documentDTO.getDocumentName());
                documentEntity.setDocumentType(documentDTO.getDocumentType());
                documentEntity.setUploadDate(documentDTO.getUploadDate());
            }
        }

        // update
        EmployeeDetailsEntity updatedEntity = employeeDetailsRepository.save(existingEntity);

        // update its particular user entity
        UserDTO userDTO = modelMapper.map(existingEntity, UserDTO.class);
        UserDTO updatedUserDTO = userService.updateUser(userDTO);

        // Return DTO
        EmployeeDetailsDTO updatedDTO = modelMapper.map(updatedEntity, EmployeeDetailsDTO.class);

//        if (updatedEntity.getUserEntity() != null) {
//            updatedDTO.setUserId(updatedEntity.getUserEntity().getUserId());
//        }

        // Set document DTOs
        List<UserDocumentDTO> documentDTOS = new ArrayList<>();

        if (updatedEntity.getUserDocumentEntities() != null &&
                !updatedEntity.getUserDocumentEntities().isEmpty()) {

            for (UserDocumentEntity documentEntity :
                    updatedEntity.getUserDocumentEntities()) {

                UserDocumentDTO dto = modelMapper.map(documentEntity, UserDocumentDTO.class);

                documentDTOS.add(dto);
            }
        }

        updatedDTO.setUserDocumentDTOS(documentDTOS);

        log.info("Exit from updateUserInformation");

        return updatedDTO;
    }

    @Override
    public EmployeeDetailsDTO getEmployeeDetailsById(Long employeeId) {
        log.info("Enter into getEmployeeDetailsById");

        EmployeeDetailsEntity existingEntity = employeeDetailsRepository.findById(employeeId)
                .orElseThrow(() ->
                        new CustomException("User information not found", HttpStatus.NOT_FOUND));

        EmployeeDetailsDTO employeeDetailsDTO =
                modelMapper.map(existingEntity, EmployeeDetailsDTO.class);

//        if (existingEntity.getUserEntity() != null) {
//            employeeDetailsDTO.setUserId(existingEntity.getUserEntity().getUserId());
//        }

        List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

        if (existingEntity.getUserDocumentEntities() != null &&
                !existingEntity.getUserDocumentEntities().isEmpty()) {

            for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                UserDocumentDTO documentDTO =
                        modelMapper.map(documentEntity, UserDocumentDTO.class);

                userDocumentDTOS.add(documentDTO);
            }
        }

        employeeDetailsDTO.setUserDocumentDTOS(userDocumentDTOS);

        log.info("Exit from getEmployeeDetailsById");

        return employeeDetailsDTO;
    }

    @Override
    public EmployeeDetailsDTO getEmployeeDetailsByUserId(Long employeeDetailsId) {
        log.info("Enter into getEmployeeDetailsByUserId");

        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();

        Optional<EmployeeDetailsEntity> employeeDetailsEntity = employeeDetailsRepository.findByUserEntity_UserId(employeeDetailsId);

        if (employeeDetailsEntity.isPresent()) {

            EmployeeDetailsEntity existingEntity = employeeDetailsEntity.get();

            employeeDetailsDTO =
                    modelMapper.map(existingEntity, EmployeeDetailsDTO.class);

//            if (existingEntity.getUserEntity() != null) {
//                employeeDetailsDTO.setUserId(existingEntity.getUserEntity().getUserId());
//            }

            List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

            if (existingEntity.getUserDocumentEntities() != null &&
                    !existingEntity.getUserDocumentEntities().isEmpty()) {

                for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                    UserDocumentDTO documentDTO =
                            modelMapper.map(documentEntity, UserDocumentDTO.class);

                    userDocumentDTOS.add(documentDTO);
                }
            }

            employeeDetailsDTO.setUserDocumentDTOS(userDocumentDTOS);
        }
        log.info("Exit from getEmployeeDetailsByEmployeeId");

        return employeeDetailsDTO;
    }

    @Override
    public String deleteEmployeeDetails(Long employeeDetailsId) {
        log.info("Enter into deleteEmployeeDetails");

        EmployeeDetailsEntity existingEntity = employeeDetailsRepository.findById(employeeDetailsId)
                .orElseThrow(() ->
                        new CustomException("User information not found", HttpStatus.NOT_FOUND));

        UserEntity user = existingEntity.getUserEntity();

        if (user != null) {

            existingEntity.setUserEntity(null);

            user.setEmployeeDetails(null);

            employeeDetailsRepository.save(existingEntity);

            userRepository.delete(user);
        }

        employeeDetailsRepository.delete(existingEntity);

        log.info("Exit from deleteEmployeeDetails");

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllEmployeeDetailsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllEmployeeDetailsByFilter");

        Page<EmployeeDetailsEntity> informationEntityPage;
        List<EmployeeDetailsEntity> userInformationEntities;
        long totalElement;

        CustomQuerySpecification<EmployeeDetailsEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            informationEntityPage = employeeDetailsRepository.findAll(customQuerySpecification, pageable);
            userInformationEntities = informationEntityPage.getContent();
            totalElement = informationEntityPage.getTotalElements();
        } else {
            userInformationEntities = employeeDetailsRepository.findAll(customQuerySpecification);
            totalElement = (long) userInformationEntities.size();
        }

        List<EmployeeDetailsDTO> EmployeeDetailsDTOS = userInformationEntities.stream()
                .map(existingEntity -> {
                    EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(existingEntity, EmployeeDetailsDTO.class);

//                    if (existingEntity.getUserEntity() != null) {
//                        employeeDetailsDTO.setUserId(existingEntity.getUserEntity().getUserId());
//                    }

                    List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

                    if (existingEntity.getUserDocumentEntities() != null &&
                            !existingEntity.getUserDocumentEntities().isEmpty()) {

                        for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                            UserDocumentDTO documentDTO =
                                    modelMapper.map(documentEntity, UserDocumentDTO.class);

                            userDocumentDTOS.add(documentDTO);
                        }
                    }

                    employeeDetailsDTO.setUserDocumentDTOS(userDocumentDTOS);

                    return employeeDetailsDTO;
                }).collect(Collectors.toUnmodifiableList());

        log.info("Exit from getAllEmployeeDetailsByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", EmployeeDetailsDTOS);
        result.put("total", totalElement);
        return result;
    }
}
