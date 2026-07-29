package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.UserDocumentDTO;
import com.flint.sample_be_springboot.dto.UserInformationDTO;
import com.flint.sample_be_springboot.entity.UserDocumentEntity;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.UserInformationEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.UserInformationRepository;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
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
public class UserInformationServiceImpl extends BaseService implements UserInformationService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserInformationDTO saveUserInformation(UserInformationDTO userInformationDTO) {
        log.info("Enter into saveUserInformation");

        if (userInformationDTO == null) {
            throw new CustomException("User information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        UserInformationEntity userInformationEntity =
                modelMapper.map(userInformationDTO, UserInformationEntity.class);

        userInformationEntity.setAuditDetails(addAuditDetails(userInformationEntity.getAuditDetails()));

        // Set User Entity
        if (userInformationDTO.getUserId() != null) {

            UserEntity userEntity = userRepository.findById(userInformationDTO.getUserId())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            userInformationEntity.setUserEntity(userEntity);
        }

        // Set User Document entities
        List<UserDocumentEntity> userDocumentEntities = new ArrayList<>();

        if (userInformationDTO.getUserDocumentDTOS() != null &&
                !userInformationDTO.getUserDocumentDTOS().isEmpty()) {

            for (UserDocumentDTO userDocumentDTO : userInformationDTO.getUserDocumentDTOS()) {

                UserDocumentEntity userDocumentEntity =
                        modelMapper.map(userDocumentDTO, UserDocumentEntity.class);

                if (userDocumentDTO.getDocument() != null) {
                    userDocumentEntity.setDocument(
                            Base64.getDecoder().decode(userDocumentDTO.getDocument()));
                }

                userDocumentEntity.setUserInformationEntity(userInformationEntity);
                userDocumentEntity.setAuditDetails(addAuditDetails(userDocumentEntity.getAuditDetails()));

                userDocumentEntities.add(userDocumentEntity);
            }
        }

        userInformationEntity.setUserDocumentEntities(userDocumentEntities);

        // Save
        UserInformationEntity savedEntity = userInformationRepository.save(userInformationEntity);

        // Return DTO
        UserInformationDTO savedDTO = modelMapper.map(savedEntity, UserInformationDTO.class);

        if (savedEntity.getUserEntity() != null) {
            savedDTO.setUserId(savedEntity.getUserEntity().getUserId());
        }

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

        return savedDTO;
    }

    @Override
    public UserInformationDTO updateUserInformation(UserInformationDTO userInformationDTO) {
        log.info("Enter into updateUserInformation");

        if (userInformationDTO == null) {
            throw new CustomException("User information cannot be null",
                    HttpStatus.PRECONDITION_FAILED);
        }

        UserInformationEntity existingEntity =
                userInformationRepository.findById(userInformationDTO.getUserInformationId())
                        .orElseThrow(() ->
                                new CustomException("User information not found",
                                        HttpStatus.NOT_FOUND));

        // Update User reference
        if (userInformationDTO.getUserId() != null) {

            UserEntity userEntity = userRepository.findById(userInformationDTO.getUserId())
                    .orElseThrow(() ->
                            new CustomException("User not found", HttpStatus.NOT_FOUND));

            existingEntity.setUserEntity(userEntity);
        }

        // Update basic fields
        existingEntity.setEmployeeCode(userInformationDTO.getEmployeeCode());
        existingEntity.setFirstName(userInformationDTO.getFirstName());
        existingEntity.setMiddleName(userInformationDTO.getMiddleName());
        existingEntity.setLastName(userInformationDTO.getLastName());
        existingEntity.setGender(userInformationDTO.getGender());
        existingEntity.setDateOfBirth(userInformationDTO.getDateOfBirth());
        existingEntity.setAddress(userInformationDTO.getAddress());
        existingEntity.setQualification(userInformationDTO.getQualification());
        existingEntity.setSpecialization(userInformationDTO.getSpecialization());
        existingEntity.setExperience(userInformationDTO.getExperience());
        existingEntity.setDesignation(userInformationDTO.getDesignation());
        existingEntity.setJoiningDate(userInformationDTO.getJoiningDate());
        existingEntity.setBloodGroup(userInformationDTO.getBloodGroup());

        existingEntity.setAuditDetails(
                addAuditDetails(existingEntity.getAuditDetails()));

        // Remove deleted documents
        Set<Long> requestDocumentIds = userInformationDTO.getUserDocumentDTOS().stream()
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

        if (userInformationDTO.getUserDocumentDTOS() != null) {

            for (UserDocumentDTO documentDTO : userInformationDTO.getUserDocumentDTOS()) {

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

                    documentEntity.setUserInformationEntity(existingEntity);

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

        // Save
        UserInformationEntity updatedEntity = userInformationRepository.save(existingEntity);

        // Return DTO
        UserInformationDTO updatedDTO = modelMapper.map(updatedEntity, UserInformationDTO.class);

        if (updatedEntity.getUserEntity() != null) {
            updatedDTO.setUserId(updatedEntity.getUserEntity().getUserId());
        }

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
    public UserInformationDTO getUserInformationById(Long userInformationId) {
        log.info("Enter into getUserInformationById");

        UserInformationEntity existingEntity = userInformationRepository.findById(userInformationId)
                .orElseThrow(() ->
                        new CustomException("User information not found", HttpStatus.NOT_FOUND));

        UserInformationDTO userInformationDTO =
                modelMapper.map(existingEntity, UserInformationDTO.class);

        if (existingEntity.getUserEntity() != null) {
            userInformationDTO.setUserId(existingEntity.getUserEntity().getUserId());
        }

        List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

        if (existingEntity.getUserDocumentEntities() != null &&
                !existingEntity.getUserDocumentEntities().isEmpty()) {

            for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                UserDocumentDTO documentDTO =
                        modelMapper.map(documentEntity, UserDocumentDTO.class);

                userDocumentDTOS.add(documentDTO);
            }
        }

        userInformationDTO.setUserDocumentDTOS(userDocumentDTOS);

        log.info("Exit from getUserInformationById");

        return userInformationDTO;
    }

    @Override
    public UserInformationDTO getUserInformationByUserId(Long userId) {
        log.info("Enter into getUserInformationById");

        UserInformationDTO userInformationDTO = new UserInformationDTO();

        Optional<UserInformationEntity> userInformationEntity = userInformationRepository.findByUserEntity_UserId(userId);

        if(userInformationEntity.isPresent()) {

            UserInformationEntity existingEntity = userInformationEntity.get();

            userInformationDTO =
                    modelMapper.map(existingEntity, UserInformationDTO.class);

            if (existingEntity.getUserEntity() != null) {
                userInformationDTO.setUserId(existingEntity.getUserEntity().getUserId());
            }

            List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

            if (existingEntity.getUserDocumentEntities() != null &&
                    !existingEntity.getUserDocumentEntities().isEmpty()) {

                for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                    UserDocumentDTO documentDTO =
                            modelMapper.map(documentEntity, UserDocumentDTO.class);

                    userDocumentDTOS.add(documentDTO);
                }
            }

            userInformationDTO.setUserDocumentDTOS(userDocumentDTOS);
        }
        log.info("Exit from getUserInformationById");

        return userInformationDTO;
    }

    @Override
    public String deleteUserInformation(Long userInformationId) {
        log.info("Enter into deleteUserInformation");

        UserInformationEntity existingEntity = userInformationRepository.findById(userInformationId)
                .orElseThrow(() ->
                        new CustomException("User information not found", HttpStatus.NOT_FOUND));

        userInformationRepository.delete(existingEntity);

        log.info("Exit from deleteUserInformation");

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllUserInformationByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllUserInformationByFilter");

        Page<UserInformationEntity> informationEntityPage;
        List<UserInformationEntity> userInformationEntities;
        long totalElement;

        CustomQuerySpecification<UserInformationEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if(paginate){
            informationEntityPage = userInformationRepository.findAll(customQuerySpecification, pageable);
            userInformationEntities = informationEntityPage.getContent();
            totalElement = informationEntityPage.getTotalElements();
        }else{
            userInformationEntities = userInformationRepository.findAll(customQuerySpecification);
            totalElement = (long) userInformationEntities.size();
        }

        List<UserInformationDTO> userInformationDTOS = userInformationEntities.stream()
                .map(existingEntity -> {
                    UserInformationDTO userInformationDTO = modelMapper.map(existingEntity, UserInformationDTO.class);

                    if (existingEntity.getUserEntity() != null) {
                        userInformationDTO.setUserId(existingEntity.getUserEntity().getUserId());
                    }

                    List<UserDocumentDTO> userDocumentDTOS = new ArrayList<>();

                    if (existingEntity.getUserDocumentEntities() != null &&
                            !existingEntity.getUserDocumentEntities().isEmpty()) {

                        for (UserDocumentEntity documentEntity : existingEntity.getUserDocumentEntities()) {

                            UserDocumentDTO documentDTO =
                                    modelMapper.map(documentEntity, UserDocumentDTO.class);

                            userDocumentDTOS.add(documentDTO);
                        }
                    }

                    userInformationDTO.setUserDocumentDTOS(userDocumentDTOS);

                    return userInformationDTO;
                }).collect(Collectors.toUnmodifiableList());

        log.info("Exit from getAllUserInformationByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", userInformationDTOS);
        result.put("total", totalElement);
        return result;
    }
}
