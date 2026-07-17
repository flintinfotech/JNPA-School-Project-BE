package com.flint.sample_be_springboot.service.websiteModuleService;

import com.flint.sample_be_springboot.dto.websiteModuleDTOS.admission.*;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.admission.*;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.websiteModuleRepository.AdmissionRepository;
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
public class AdmissionServiceImpl extends BaseService implements AdmissionService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AdmissionRepository admissionRepository;

    @Override
    public AdmissionDTO saveAdmission(AdmissionDTO admissionDTO) {
        log.info("Enter into saveAdmission");

        AdmissionEntity isExist = admissionRepository.findByClassRoomNameAndAcademicYearNameAndMedium
                (admissionDTO.getClassRoomName(), admissionDTO.getAcademicYearName(), admissionDTO.getMedium());

        if (isExist != null) {
            throw new CustomException("Admission process details are already exists", HttpStatus.PRECONDITION_FAILED);
        }

        AdmissionEntity admissionEntity = modelMapper.map(admissionDTO, AdmissionEntity.class);
        admissionEntity.setAuditDetails(addAuditDetails(admissionEntity.getAuditDetails()));

        if (admissionDTO.getBrochure() != null) {
            admissionEntity.setBrochure(Base64.getDecoder().decode(admissionDTO.getBrochure()));
        }

        // Set Eligibility Criteria
        List<EligibilityCriteriaEntity> eligibilityCriteriaEntities = new ArrayList<>();
        if (admissionDTO.getEligibilityCriteriaDTOS() != null &&
                !admissionDTO.getEligibilityCriteriaDTOS().isEmpty()) {

            for (EligibilityCriteriaDTO eligibilityCriteriaDTO : admissionDTO.getEligibilityCriteriaDTOS()) {

                EligibilityCriteriaEntity eligibilityCriteriaEntity =
                        modelMapper.map(eligibilityCriteriaDTO, EligibilityCriteriaEntity.class);

                eligibilityCriteriaEntity.setAuditDetails(addAuditDetails(admissionEntity.getAuditDetails()));
                eligibilityCriteriaEntity.setAdmissionEntity(admissionEntity);

                eligibilityCriteriaEntities.add(eligibilityCriteriaEntity);
            }
        }
        admissionEntity.setEligibilityCriteriaEntities(eligibilityCriteriaEntities);

        // Set Important Dates
        List<ImportantDateEntity> importantDateEntities = new ArrayList<>();
        if (admissionDTO.getImportantDateDTOS() != null &&
                !admissionDTO.getImportantDateDTOS().isEmpty()) {

            for (ImportantDateDTO importantDateDTO : admissionDTO.getImportantDateDTOS()) {

                ImportantDateEntity importantDateEntity =
                        modelMapper.map(importantDateDTO, ImportantDateEntity.class);

                importantDateEntity.setAuditDetails(addAuditDetails(admissionEntity.getAuditDetails()));
                importantDateEntity.setAdmissionEntity(admissionEntity);

                importantDateEntities.add(importantDateEntity);
            }
        }
        admissionEntity.setImportantDatsEntities(importantDateEntities);

        // Set Required Documents
        List<RequiredDocumentEntity> requiredDocumentEntities = new ArrayList<>();
        if (admissionDTO.getRequiredDocumentDTOS() != null &&
                !admissionDTO.getRequiredDocumentDTOS().isEmpty()) {

            for (RequiredDocumentDTO requiredDocumentDTO : admissionDTO.getRequiredDocumentDTOS()) {

                RequiredDocumentEntity requiredDocumentEntity =
                        modelMapper.map(requiredDocumentDTO, RequiredDocumentEntity.class);

                requiredDocumentEntity.setAuditDetails(addAuditDetails(admissionEntity.getAuditDetails()));
                requiredDocumentEntity.setAdmissionEntity(admissionEntity);

                requiredDocumentEntities.add(requiredDocumentEntity);
            }
        }
        admissionEntity.setRequireDocumentsEntities(requiredDocumentEntities);

        // Set Admission Process
        List<AdmissionProcessEntity> admissionProcessEntities = new ArrayList<>();
        if (admissionDTO.getAdmissionProcessDTOS() != null &&
                !admissionDTO.getAdmissionProcessDTOS().isEmpty()) {

            for (AdmissionProcessDTO admissionProcessDTO : admissionDTO.getAdmissionProcessDTOS()) {

                AdmissionProcessEntity admissionProcessEntity =
                        modelMapper.map(admissionProcessDTO, AdmissionProcessEntity.class);

                admissionProcessEntity.setAuditDetails(addAuditDetails(admissionEntity.getAuditDetails()));
                admissionProcessEntity.setAdmissionEntity(admissionEntity);

                admissionProcessEntities.add(admissionProcessEntity);
            }
        }
        admissionEntity.setAdmissionProcessEntities(admissionProcessEntities);

        AdmissionEntity savedAdmissionEntity = admissionRepository.save(admissionEntity);

        // Set Eligibility DTOs
        List<EligibilityCriteriaDTO> eligibilityCriteriaDTOS = new ArrayList<>();
        if (savedAdmissionEntity.getEligibilityCriteriaEntities() != null &&
                !savedAdmissionEntity.getEligibilityCriteriaEntities().isEmpty()) {

            for (EligibilityCriteriaEntity eligibilityCriteriaEntity :
                    savedAdmissionEntity.getEligibilityCriteriaEntities()) {

                EligibilityCriteriaDTO eligibilityCriteriaDTO =
                        modelMapper.map(eligibilityCriteriaEntity, EligibilityCriteriaDTO.class);

                eligibilityCriteriaDTO.setAdmissionId(savedAdmissionEntity.getAdmissionId());

                eligibilityCriteriaDTOS.add(eligibilityCriteriaDTO);
            }
        }

        // Set Important Date DTOs
        List<ImportantDateDTO> importantDateDTOS = new ArrayList<>();
        if (savedAdmissionEntity.getImportantDatsEntities() != null &&
                !savedAdmissionEntity.getImportantDatsEntities().isEmpty()) {

            for (ImportantDateEntity importantDateEntity :
                    savedAdmissionEntity.getImportantDatsEntities()) {

                ImportantDateDTO importantDateDTO =
                        modelMapper.map(importantDateEntity, ImportantDateDTO.class);

                importantDateDTO.setAdmissionId(savedAdmissionEntity.getAdmissionId());

                importantDateDTOS.add(importantDateDTO);
            }
        }

        // Set Required Document DTOs
        List<RequiredDocumentDTO> requiredDocumentDTOS = new ArrayList<>();
        if (savedAdmissionEntity.getRequireDocumentsEntities() != null &&
                !savedAdmissionEntity.getRequireDocumentsEntities().isEmpty()) {

            for (RequiredDocumentEntity requiredDocumentEntity :
                    savedAdmissionEntity.getRequireDocumentsEntities()) {

                RequiredDocumentDTO requiredDocumentDTO =
                        modelMapper.map(requiredDocumentEntity, RequiredDocumentDTO.class);

                requiredDocumentDTO.setAdmissionId(savedAdmissionEntity.getAdmissionId());

                requiredDocumentDTOS.add(requiredDocumentDTO);
            }
        }

        // Set Admission Process DTOs
        List<AdmissionProcessDTO> admissionProcessDTOS = new ArrayList<>();
        if (savedAdmissionEntity.getAdmissionProcessEntities() != null &&
                !savedAdmissionEntity.getAdmissionProcessEntities().isEmpty()) {

            for (AdmissionProcessEntity admissionProcessEntity :
                    savedAdmissionEntity.getAdmissionProcessEntities()) {

                AdmissionProcessDTO admissionProcessDTO =
                        modelMapper.map(admissionProcessEntity, AdmissionProcessDTO.class);

                admissionProcessDTO.setAdmissionId(savedAdmissionEntity.getAdmissionId());

                admissionProcessDTOS.add(admissionProcessDTO);
            }
        }

        AdmissionDTO savedDTO = modelMapper.map(savedAdmissionEntity, AdmissionDTO.class);

        savedDTO.setEligibilityCriteriaDTOS(eligibilityCriteriaDTOS);
        savedDTO.setImportantDateDTOS(importantDateDTOS);
        savedDTO.setRequiredDocumentDTOS(requiredDocumentDTOS);
        savedDTO.setAdmissionProcessDTOS(admissionProcessDTOS);

        log.info("Exit from saveAdmission");

        return savedDTO;

    }

    @Override
    public AdmissionDTO updateAdmission(AdmissionDTO admissionDTO) {
        log.info("Enter into updateAdmission");

        AdmissionEntity existingEntity = admissionRepository.findById(admissionDTO.getAdmissionId())
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        admissionRepository.findByClassRoomNameAndAcademicYearNameAndMediumAndAdmissionIdNot
                        (admissionDTO.getClassRoomName(), admissionDTO.getAcademicYearName(), admissionDTO.getMedium(), admissionDTO.getAdmissionId())
                .ifPresent(entity -> {
                    throw new CustomException("Admission details are already exists", HttpStatus.PRECONDITION_FAILED);
                });

        AuditDetails auditDetails = existingEntity.getAuditDetails();

        modelMapper.map(admissionDTO, existingEntity);
        existingEntity.setAuditDetails(addAuditDetails(auditDetails));

        // Set Eligibility Criteria entities

        Set<Long> requestEligibilityIds = admissionDTO.getEligibilityCriteriaDTOS().stream()
                .map(EligibilityCriteriaDTO::getEligibilityCriteriaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getEligibilityCriteriaEntities().removeIf(criteria ->
                criteria.getEligibilityCriteriaId() != null &&
                        !requestEligibilityIds.contains(criteria.getEligibilityCriteriaId()));

        Map<Long, EligibilityCriteriaEntity> eligibilityMap =
                existingEntity.getEligibilityCriteriaEntities().stream()
                        .collect(Collectors.toMap(
                                EligibilityCriteriaEntity::getEligibilityCriteriaId,
                                Function.identity()
                        ));

        if (admissionDTO.getEligibilityCriteriaDTOS() != null &&
                !admissionDTO.getEligibilityCriteriaDTOS().isEmpty()) {

            for (EligibilityCriteriaDTO eligibilityCriteriaDTO :
                    admissionDTO.getEligibilityCriteriaDTOS()) {

                EligibilityCriteriaEntity eligibilityCriteriaEntity;

                // Update existing
                if (eligibilityCriteriaDTO.getEligibilityCriteriaId() != null &&
                        eligibilityMap.containsKey(eligibilityCriteriaDTO.getEligibilityCriteriaId())) {

                    eligibilityCriteriaEntity =
                            eligibilityMap.get(eligibilityCriteriaDTO.getEligibilityCriteriaId());

                    AuditDetails auditDetails1 = eligibilityCriteriaEntity.getAuditDetails();

                    modelMapper.map(eligibilityCriteriaDTO, eligibilityCriteriaEntity);

                    eligibilityCriteriaEntity.setAuditDetails(auditDetails1);
                    eligibilityCriteriaEntity.setAdmissionEntity(existingEntity);
                }

                // Add new
                else {

                    eligibilityCriteriaEntity =
                            modelMapper.map(eligibilityCriteriaDTO, EligibilityCriteriaEntity.class);

                    eligibilityCriteriaEntity.setAdmissionEntity(existingEntity);
                    eligibilityCriteriaEntity.setAuditDetails(
                            addAuditDetails(eligibilityCriteriaEntity.getAuditDetails()));

                    existingEntity.getEligibilityCriteriaEntities().add(eligibilityCriteriaEntity);
                }
            }
        }

        // Set Important Date entities

        Set<Long> requestImportantDateIds = admissionDTO.getImportantDateDTOS().stream()
                .map(ImportantDateDTO::getImportantDateId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getImportantDatsEntities().removeIf(importantDate ->
                importantDate.getImportantDateId() != null &&
                        !requestImportantDateIds.contains(importantDate.getImportantDateId()));

        Map<Long, ImportantDateEntity> importantDateMap =
                existingEntity.getImportantDatsEntities().stream()
                        .collect(Collectors.toMap(
                                ImportantDateEntity::getImportantDateId,
                                Function.identity()
                        ));

        if (admissionDTO.getImportantDateDTOS() != null &&
                !admissionDTO.getImportantDateDTOS().isEmpty()) {

            for (ImportantDateDTO importantDateDTO : admissionDTO.getImportantDateDTOS()) {

                ImportantDateEntity importantDateEntity;

                // Update existing
                if (importantDateDTO.getImportantDateId() != null &&
                        importantDateMap.containsKey(importantDateDTO.getImportantDateId())) {

                    importantDateEntity =
                            importantDateMap.get(importantDateDTO.getImportantDateId());

                    AuditDetails auditDetails2 = importantDateEntity.getAuditDetails();

                    modelMapper.map(importantDateDTO, importantDateEntity);

                    importantDateEntity.setAuditDetails(auditDetails2);
                    importantDateEntity.setAdmissionEntity(existingEntity);
                }

                // Add new
                else {

                    importantDateEntity =
                            modelMapper.map(importantDateDTO, ImportantDateEntity.class);

                    importantDateEntity.setAdmissionEntity(existingEntity);
                    importantDateEntity.setAuditDetails(
                            addAuditDetails(importantDateEntity.getAuditDetails()));

                    existingEntity.getImportantDatsEntities().add(importantDateEntity);
                }
            }
        }

        // Set Required Document entities

        Set<Long> requestRequiredDocumentIds = admissionDTO.getRequiredDocumentDTOS().stream()
                .map(RequiredDocumentDTO::getRequiredDocumentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getRequireDocumentsEntities().removeIf(document ->
                document.getRequiredDocumentId() != null &&
                        !requestRequiredDocumentIds.contains(document.getRequiredDocumentId()));

        Map<Long, RequiredDocumentEntity> requiredDocumentMap =
                existingEntity.getRequireDocumentsEntities().stream()
                        .collect(Collectors.toMap(
                                RequiredDocumentEntity::getRequiredDocumentId,
                                Function.identity()
                        ));

        if (admissionDTO.getRequiredDocumentDTOS() != null &&
                !admissionDTO.getRequiredDocumentDTOS().isEmpty()) {

            for (RequiredDocumentDTO requiredDocumentDTO : admissionDTO.getRequiredDocumentDTOS()) {

                RequiredDocumentEntity requiredDocumentEntity;

                // Update existing
                if (requiredDocumentDTO.getRequiredDocumentId() != null &&
                        requiredDocumentMap.containsKey(requiredDocumentDTO.getRequiredDocumentId())) {

                    requiredDocumentEntity =
                            requiredDocumentMap.get(requiredDocumentDTO.getRequiredDocumentId());

                    AuditDetails auditDetails3 = requiredDocumentEntity.getAuditDetails();

                    modelMapper.map(requiredDocumentDTO, requiredDocumentEntity);

                    requiredDocumentEntity.setAuditDetails(auditDetails3);
                    requiredDocumentEntity.setAdmissionEntity(existingEntity);
                }

                // Add new
                else {

                    requiredDocumentEntity =
                            modelMapper.map(requiredDocumentDTO, RequiredDocumentEntity.class);

                    requiredDocumentEntity.setAdmissionEntity(existingEntity);
                    requiredDocumentEntity.setAuditDetails(
                            addAuditDetails(requiredDocumentEntity.getAuditDetails()));

                    existingEntity.getRequireDocumentsEntities().add(requiredDocumentEntity);
                }
            }
        }

        // Set Admission Process entities

        Set<Long> requestAdmissionProcessIds = admissionDTO.getAdmissionProcessDTOS().stream()
                .map(AdmissionProcessDTO::getAdmissionProcessId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getAdmissionProcessEntities().removeIf(process ->
                process.getAdmissionProcessId() != null &&
                        !requestAdmissionProcessIds.contains(process.getAdmissionProcessId()));

        Map<Long, AdmissionProcessEntity> admissionProcessMap =
                existingEntity.getAdmissionProcessEntities().stream()
                        .collect(Collectors.toMap(
                                AdmissionProcessEntity::getAdmissionProcessId,
                                Function.identity()
                        ));

        if (admissionDTO.getAdmissionProcessDTOS() != null &&
                !admissionDTO.getAdmissionProcessDTOS().isEmpty()) {

            for (AdmissionProcessDTO admissionProcessDTO : admissionDTO.getAdmissionProcessDTOS()) {

                AdmissionProcessEntity admissionProcessEntity;

                // Update existing
                if (admissionProcessDTO.getAdmissionProcessId() != null &&
                        admissionProcessMap.containsKey(admissionProcessDTO.getAdmissionProcessId())) {

                    admissionProcessEntity =
                            admissionProcessMap.get(admissionProcessDTO.getAdmissionProcessId());

                    AuditDetails auditDetails4 = admissionProcessEntity.getAuditDetails();

                    modelMapper.map(admissionProcessDTO, admissionProcessEntity);

                    admissionProcessEntity.setAuditDetails(auditDetails4);
                    admissionProcessEntity.setAdmissionEntity(existingEntity);
                }

                // Add new
                else {

                    admissionProcessEntity =
                            modelMapper.map(admissionProcessDTO, AdmissionProcessEntity.class);

                    admissionProcessEntity.setAdmissionEntity(existingEntity);
                    admissionProcessEntity.setAuditDetails(
                            addAuditDetails(admissionProcessEntity.getAuditDetails()));

                    existingEntity.getAdmissionProcessEntities().add(admissionProcessEntity);
                }
            }
        }

        if (admissionDTO.getBrochure() != null) {
            existingEntity.setBrochure(Base64.getDecoder().decode(admissionDTO.getBrochure()));
        } else {
            existingEntity.setBrochure(null);
        }

        AdmissionEntity updatedAdmissionEntity = admissionRepository.save(existingEntity);

        // Set Eligibility Criteria DTOs
        List<EligibilityCriteriaDTO> eligibilityCriteriaDTOS = new ArrayList<>();
        if (updatedAdmissionEntity.getEligibilityCriteriaEntities() != null &&
                !updatedAdmissionEntity.getEligibilityCriteriaEntities().isEmpty()) {

            for (EligibilityCriteriaEntity eligibilityCriteriaEntity :
                    updatedAdmissionEntity.getEligibilityCriteriaEntities()) {

                EligibilityCriteriaDTO eligibilityCriteriaDTO =
                        modelMapper.map(eligibilityCriteriaEntity, EligibilityCriteriaDTO.class);

                eligibilityCriteriaDTO.setAdmissionId(updatedAdmissionEntity.getAdmissionId());

                eligibilityCriteriaDTOS.add(eligibilityCriteriaDTO);
            }
        }

        // Set Important Date DTOs
        List<ImportantDateDTO> importantDateDTOS = new ArrayList<>();
        if (updatedAdmissionEntity.getImportantDatsEntities() != null &&
                !updatedAdmissionEntity.getImportantDatsEntities().isEmpty()) {

            for (ImportantDateEntity importantDateEntity :
                    updatedAdmissionEntity.getImportantDatsEntities()) {

                ImportantDateDTO importantDateDTO =
                        modelMapper.map(importantDateEntity, ImportantDateDTO.class);

                importantDateDTO.setAdmissionId(updatedAdmissionEntity.getAdmissionId());

                importantDateDTOS.add(importantDateDTO);
            }
        }

        // Set Required Document DTOs
        List<RequiredDocumentDTO> requiredDocumentDTOS = new ArrayList<>();
        if (updatedAdmissionEntity.getRequireDocumentsEntities() != null &&
                !updatedAdmissionEntity.getRequireDocumentsEntities().isEmpty()) {

            for (RequiredDocumentEntity requiredDocumentEntity :
                    updatedAdmissionEntity.getRequireDocumentsEntities()) {

                RequiredDocumentDTO requiredDocumentDTO =
                        modelMapper.map(requiredDocumentEntity, RequiredDocumentDTO.class);

                requiredDocumentDTO.setAdmissionId(updatedAdmissionEntity.getAdmissionId());

                requiredDocumentDTOS.add(requiredDocumentDTO);
            }
        }

        // Set Admission Process DTOs
        List<AdmissionProcessDTO> admissionProcessDTOS = new ArrayList<>();
        if (updatedAdmissionEntity.getAdmissionProcessEntities() != null &&
                !updatedAdmissionEntity.getAdmissionProcessEntities().isEmpty()) {

            for (AdmissionProcessEntity admissionProcessEntity :
                    updatedAdmissionEntity.getAdmissionProcessEntities()) {

                AdmissionProcessDTO admissionProcessDTO =
                        modelMapper.map(admissionProcessEntity, AdmissionProcessDTO.class);

                admissionProcessDTO.setAdmissionId(updatedAdmissionEntity.getAdmissionId());

                admissionProcessDTOS.add(admissionProcessDTO);
            }
        }

        AdmissionDTO updatedDTO = modelMapper.map(updatedAdmissionEntity, AdmissionDTO.class);

        updatedDTO.setEligibilityCriteriaDTOS(eligibilityCriteriaDTOS);
        updatedDTO.setImportantDateDTOS(importantDateDTOS);
        updatedDTO.setRequiredDocumentDTOS(requiredDocumentDTOS);
        updatedDTO.setAdmissionProcessDTOS(admissionProcessDTOS);

        log.info("Exit from updateAdmission");

        return updatedDTO;

    }

    @Override
    public String deleteAdmission(Long admissionId) {
        log.info("Enter into deleteAdmission");

        AdmissionEntity admissionEntity = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        // validations

        admissionRepository.delete(admissionEntity);

        log.info("Exit from deleteAdmission");
        return "Record deleted successfully";
    }

    @Override
    public AdmissionDTO getAdmissionById(Long admissionId) {
        log.info("Enter into getAdmissionById");

        AdmissionEntity admissionEntity = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        AdmissionDTO admissionDTO = modelMapper.map(admissionEntity, AdmissionDTO.class);

        List<EligibilityCriteriaDTO> eligibilityCriteriaDTOS = new ArrayList<>();
        if (admissionEntity.getEligibilityCriteriaEntities() != null &&
                !admissionEntity.getEligibilityCriteriaEntities().isEmpty()) {

            for (EligibilityCriteriaEntity eligibilityCriteriaEntity :
                    admissionEntity.getEligibilityCriteriaEntities()) {

                EligibilityCriteriaDTO eligibilityCriteriaDTO =
                        modelMapper.map(eligibilityCriteriaEntity, EligibilityCriteriaDTO.class);

                eligibilityCriteriaDTO.setAdmissionId(admissionEntity.getAdmissionId());

                eligibilityCriteriaDTOS.add(eligibilityCriteriaDTO);
            }
        }

        // Set Important Date DTOs
        List<ImportantDateDTO> importantDateDTOS = new ArrayList<>();
        if (admissionEntity.getImportantDatsEntities() != null &&
                !admissionEntity.getImportantDatsEntities().isEmpty()) {

            for (ImportantDateEntity importantDateEntity :
                    admissionEntity.getImportantDatsEntities()) {

                ImportantDateDTO importantDateDTO =
                        modelMapper.map(importantDateEntity, ImportantDateDTO.class);

                importantDateDTO.setAdmissionId(admissionEntity.getAdmissionId());

                importantDateDTOS.add(importantDateDTO);
            }
        }

        // Set Required Document DTOs
        List<RequiredDocumentDTO> requiredDocumentDTOS = new ArrayList<>();
        if (admissionEntity.getRequireDocumentsEntities() != null &&
                !admissionEntity.getRequireDocumentsEntities().isEmpty()) {

            for (RequiredDocumentEntity requiredDocumentEntity :
                    admissionEntity.getRequireDocumentsEntities()) {

                RequiredDocumentDTO requiredDocumentDTO =
                        modelMapper.map(requiredDocumentEntity, RequiredDocumentDTO.class);

                requiredDocumentDTO.setAdmissionId(admissionEntity.getAdmissionId());

                requiredDocumentDTOS.add(requiredDocumentDTO);
            }
        }

        // Set Admission Process DTOs
        List<AdmissionProcessDTO> admissionProcessDTOS = new ArrayList<>();
        if (admissionEntity.getAdmissionProcessEntities() != null &&
                !admissionEntity.getAdmissionProcessEntities().isEmpty()) {

            for (AdmissionProcessEntity admissionProcessEntity :
                    admissionEntity.getAdmissionProcessEntities()) {

                AdmissionProcessDTO admissionProcessDTO =
                        modelMapper.map(admissionProcessEntity, AdmissionProcessDTO.class);

                admissionProcessDTO.setAdmissionId(admissionEntity.getAdmissionId());

                admissionProcessDTOS.add(admissionProcessDTO);
            }
        }

        admissionDTO.setEligibilityCriteriaDTOS(eligibilityCriteriaDTOS);
        admissionDTO.setImportantDateDTOS(importantDateDTOS);
        admissionDTO.setRequiredDocumentDTOS(requiredDocumentDTOS);
        admissionDTO.setAdmissionProcessDTOS(admissionProcessDTOS);

        log.info("Exit from getAdmissionById");

        return admissionDTO;
    }

    @Override
    public Map<String, Object> getAllAdmissionByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllExamsByFilter");

        List<AdmissionEntity> admissionEntities;
        Page<AdmissionEntity> admissionEntityPage;
        long totalElements;

        CustomQuerySpecification<AdmissionEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            admissionEntityPage = admissionRepository.findAll(customQuerySpecification, pageable);
            admissionEntities = admissionEntityPage.getContent();
            totalElements = admissionEntityPage.getTotalElements();
        } else {
            admissionEntities = admissionRepository.findAll(customQuerySpecification);
            totalElements = admissionEntities.size();
        }

        List<AdmissionDTO> admissionDTOS = admissionEntities.stream()
                .map(admissionEntity -> {
                    AdmissionDTO dto = modelMapper.map(admissionEntity, AdmissionDTO.class);

                    List<EligibilityCriteriaDTO> eligibilityCriteriaDTOS = new ArrayList<>();
                    if (admissionEntity.getEligibilityCriteriaEntities() != null &&
                            !admissionEntity.getEligibilityCriteriaEntities().isEmpty()) {

                        for (EligibilityCriteriaEntity eligibilityCriteriaEntity :
                                admissionEntity.getEligibilityCriteriaEntities()) {

                            EligibilityCriteriaDTO eligibilityCriteriaDTO =
                                    modelMapper.map(eligibilityCriteriaEntity, EligibilityCriteriaDTO.class);

                            eligibilityCriteriaDTO.setAdmissionId(admissionEntity.getAdmissionId());

                            eligibilityCriteriaDTOS.add(eligibilityCriteriaDTO);
                        }
                    }

                    // Set Important Date DTOs
                    List<ImportantDateDTO> importantDateDTOS = new ArrayList<>();
                    if (admissionEntity.getImportantDatsEntities() != null &&
                            !admissionEntity.getImportantDatsEntities().isEmpty()) {

                        for (ImportantDateEntity importantDateEntity :
                                admissionEntity.getImportantDatsEntities()) {

                            ImportantDateDTO importantDateDTO =
                                    modelMapper.map(importantDateEntity, ImportantDateDTO.class);

                            importantDateDTO.setAdmissionId(admissionEntity.getAdmissionId());

                            importantDateDTOS.add(importantDateDTO);
                        }
                    }

                    // Set Required Document DTOs
                    List<RequiredDocumentDTO> requiredDocumentDTOS = new ArrayList<>();
                    if (admissionEntity.getRequireDocumentsEntities() != null &&
                            !admissionEntity.getRequireDocumentsEntities().isEmpty()) {

                        for (RequiredDocumentEntity requiredDocumentEntity :
                                admissionEntity.getRequireDocumentsEntities()) {

                            RequiredDocumentDTO requiredDocumentDTO =
                                    modelMapper.map(requiredDocumentEntity, RequiredDocumentDTO.class);

                            requiredDocumentDTO.setAdmissionId(admissionEntity.getAdmissionId());

                            requiredDocumentDTOS.add(requiredDocumentDTO);
                        }
                    }

                    // Set Admission Process DTOs
                    List<AdmissionProcessDTO> admissionProcessDTOS = new ArrayList<>();
                    if (admissionEntity.getAdmissionProcessEntities() != null &&
                            !admissionEntity.getAdmissionProcessEntities().isEmpty()) {

                        for (AdmissionProcessEntity admissionProcessEntity :
                                admissionEntity.getAdmissionProcessEntities()) {

                            AdmissionProcessDTO admissionProcessDTO =
                                    modelMapper.map(admissionProcessEntity, AdmissionProcessDTO.class);

                            admissionProcessDTO.setAdmissionId(admissionEntity.getAdmissionId());

                            admissionProcessDTOS.add(admissionProcessDTO);
                        }
                    }

                    dto.setEligibilityCriteriaDTOS(eligibilityCriteriaDTOS);
                    dto.setImportantDateDTOS(importantDateDTOS);
                    dto.setRequiredDocumentDTOS(requiredDocumentDTOS);
                    dto.setAdmissionProcessDTOS(admissionProcessDTOS);

                    return dto;
                }).collect(Collectors.toList());

        log.info("Enter into getAllExamsByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("AdmissionDTOS", admissionDTOS);
        map.put("total elements", totalElements);
        return map;
    }

}
