package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.AcademicYearDTO;
import com.flint.sample_be_springboot.dto.SubScreenDTO;
import com.flint.sample_be_springboot.dto.SubScreenDataDTO;
import com.flint.sample_be_springboot.entity.AcademicYearEntity;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.SubScreenDataEntity;
import com.flint.sample_be_springboot.entity.SubScreenEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.AcademicYearRepository;
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
public class AcademicServiceImpl extends BaseService implements AcademicYearService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    AcademicYearRepository academicYearRepository;

    @Override
    public AcademicYearDTO saveAcademicYear(AcademicYearDTO academicYearDTO) {
        log.info("Enter into saveAcademicYear");

        AcademicYearEntity academicYearEntity = academicYearRepository.findByAcademicYearName(academicYearDTO.getAcademicYearName());
        if(academicYearEntity != null){
            throw new CustomException("This year is already present", HttpStatus.PRECONDITION_FAILED);
        }

        // other validations

        AcademicYearEntity academicYear = modelMapper.map(academicYearDTO, AcademicYearEntity.class);
        academicYear.setAuditDetails(addAuditDetails(academicYear.getAuditDetails()));

        List<SubScreenEntity> subScreenEntities = new ArrayList<>();
        if (academicYearDTO.getSubScreenDTOS() != null && !academicYearDTO.getSubScreenDTOS().isEmpty()) {
            for (SubScreenDTO dto : academicYearDTO.getSubScreenDTOS()) {
                SubScreenEntity subScreenEntity = new SubScreenEntity();
                subScreenEntity.setSubScreenName(dto.getSubScreenName());

                List<SubScreenDataEntity> subScreenDataEntities = new ArrayList<>();
                for (SubScreenDataDTO dataDTO : dto.getSubScreenDataEntities()) {
                    SubScreenDataEntity subScreenDataEntity = new SubScreenDataEntity();

                    subScreenDataEntity.setSubjectName(dataDTO.getSubjectName());
                    subScreenDataEntity.setSubScreenEntity(subScreenEntity);

                    if (dataDTO.getSubjectData() != null) {
                        subScreenDataEntity.setSubjectData(Base64.getDecoder().decode(dataDTO.getSubjectData()));
                    }

                    subScreenEntity.setAcademicYearEntity(academicYear);
                    subScreenDataEntities.add(subScreenDataEntity);
                }
                subScreenEntity.setSubScreenDataEntities(subScreenDataEntities);
                subScreenEntities.add(subScreenEntity);
            }
        }

        academicYear.setSubScreenEntities(subScreenEntities);

        AcademicYearEntity savedEntity = academicYearRepository.save(academicYear);

        List<SubScreenDTO> subScreenDTOS = savedEntity.getSubScreenEntities().stream()
                .map(s -> {
                    SubScreenDTO subScreenDTO = modelMapper.map(s, SubScreenDTO.class);
                    subScreenDTO.setAcademicYearId(savedEntity.getAcademicYearId());

                    List<SubScreenDataDTO> subScreenDataDTOS = s.getSubScreenDataEntities().stream()
                            .map(sd ->{
                                SubScreenDataDTO subScreenDataDTO = modelMapper.map(sd, SubScreenDataDTO.class);
                                subScreenDataDTO.setSubScreenId(s.getSubScreenId());
                                return subScreenDataDTO;
                            }).collect(Collectors.toUnmodifiableList());

                    subScreenDTO.setSubScreenDataEntities(subScreenDataDTOS);
                    return subScreenDTO;
                }).collect(Collectors.toUnmodifiableList());

        AcademicYearDTO savedDTO = modelMapper.map(savedEntity, AcademicYearDTO.class);
        savedDTO.setSubScreenDTOS(subScreenDTOS);

        log.info("Exit from saveAcademicYear");
        return savedDTO;
    }

//    @Override
//    public AcademicYearDTO updateAcademicYear(AcademicYearDTO academicYearDTO) {
//        log.info("Enter into updateAcademicYear");
//
//        AcademicYearEntity existingAcademicYear = academicYearRepository.findById(academicYearDTO.getAcademicYearId())
//                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));
//
//        academicYearRepository
//                .findByAcademicYearNameAndAcademicYearIdNot(
//                        academicYearDTO.getAcademicYearName(),
//                        existingAcademicYear.getAcademicYearId())
//                .ifPresent(entity -> {
//                    throw new CustomException(
//                            "This academic year already exists",
//                            HttpStatus.PRECONDITION_FAILED
//                    );
//                });
//
//        AuditDetails auditDetails = existingAcademicYear.getAuditDetails();
//
//        modelMapper.map(academicYearDTO, AcademicYearEntity.class);
//        existingAcademicYear.setAuditDetails(addAuditDetails(auditDetails));
//
//        AcademicYearEntity updatedEntity = academicYearRepository.save(existingAcademicYear);
//        AcademicYearDTO updatedDTO = modelMapper.map(updatedEntity, AcademicYearDTO.class);
//
//        log.info("Exit from updateAcademicYear");
//        return updatedDTO;
//    }

    @Override
    public AcademicYearDTO updateAcademicYear(AcademicYearDTO academicYearDTO) {

        log.info("Enter into updateAcademicYear");

        AcademicYearEntity existingEntity = academicYearRepository.findById(academicYearDTO.getAcademicYearId())
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.NOT_FOUND));

        academicYearRepository
                .findByAcademicYearNameAndAcademicYearIdNot(
                        academicYearDTO.getAcademicYearName(),
                        existingEntity.getAcademicYearId())
                .ifPresent(entity -> {
                    throw new CustomException(
                            "This academic year already exists",
                            HttpStatus.PRECONDITION_FAILED
                    );
                });

        AuditDetails auditDetails = existingEntity.getAuditDetails();

        modelMapper.map(academicYearDTO, existingEntity);

        existingEntity.setAuditDetails(addAuditDetails(auditDetails));

        // ============================
        // Delete removed Sub Screens
        // ============================

        Set<Long> requestSubScreenIds = academicYearDTO.getSubScreenDTOS().stream()
                .map(SubScreenDTO::getSubScreenId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getSubScreenEntities().removeIf(subScreen ->
                subScreen.getSubScreenId() != null &&
                        !requestSubScreenIds.contains(subScreen.getSubScreenId()));

        // Existing SubScreen map

        Map<Long, SubScreenEntity> existingSubScreens =
                existingEntity.getSubScreenEntities().stream()
                        .collect(Collectors.toMap(
                                SubScreenEntity::getSubScreenId,
                                Function.identity()));

        for (SubScreenDTO subScreenDTO : academicYearDTO.getSubScreenDTOS()) {

            SubScreenEntity subScreenEntity;

            if (subScreenDTO.getSubScreenId() != null &&
                    existingSubScreens.containsKey(subScreenDTO.getSubScreenId())) {

                subScreenEntity = existingSubScreens.get(subScreenDTO.getSubScreenId());

            } else {

                subScreenEntity = new SubScreenEntity();

                subScreenEntity.setAcademicYearEntity(existingEntity);

                existingEntity.getSubScreenEntities().add(subScreenEntity);
            }

            subScreenEntity.setSubScreenName(subScreenDTO.getSubScreenName());

            Set<Long> requestDataIds = subScreenDTO.getSubScreenDataEntities().stream()
                    .map(SubScreenDataDTO::getSubScreenDataId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            subScreenEntity.getSubScreenDataEntities().removeIf(data ->
                    data.getSubScreenDataId() != null &&
                            !requestDataIds.contains(data.getSubScreenDataId()));

            Map<Long, SubScreenDataEntity> existingDataMap =
                    subScreenEntity.getSubScreenDataEntities().stream()
                            .collect(Collectors.toMap(
                                    SubScreenDataEntity::getSubScreenDataId,
                                    Function.identity()));

            for (SubScreenDataDTO dataDTO : subScreenDTO.getSubScreenDataEntities()) {

                SubScreenDataEntity dataEntity;

                if (dataDTO.getSubScreenDataId() != null &&
                        existingDataMap.containsKey(dataDTO.getSubScreenDataId())) {

                    dataEntity = existingDataMap.get(dataDTO.getSubScreenDataId());

                } else {

                    dataEntity = new SubScreenDataEntity();

                    dataEntity.setSubScreenEntity(subScreenEntity);

                    subScreenEntity.getSubScreenDataEntities().add(dataEntity);
                }

                dataEntity.setSubjectName(dataDTO.getSubjectName());

                if (dataDTO.getSubjectData() != null) {
                    dataEntity.setSubjectData(
                            Base64.getDecoder().decode(dataDTO.getSubjectData()));
                }
            }
        }

        AcademicYearEntity updatedEntity = academicYearRepository.save(existingEntity);

        List<SubScreenDTO> subScreenDTOS = updatedEntity.getSubScreenEntities().stream()
                .map(s -> {
                    SubScreenDTO subScreenDTO = modelMapper.map(s, SubScreenDTO.class);
                    subScreenDTO.setAcademicYearId(updatedEntity.getAcademicYearId());

                    List<SubScreenDataDTO> subScreenDataDTOS = s.getSubScreenDataEntities().stream()
                            .map(sd ->{
                                SubScreenDataDTO subScreenDataDTO = modelMapper.map(sd, SubScreenDataDTO.class);
                                subScreenDataDTO.setSubScreenId(s.getSubScreenId());
                                return subScreenDataDTO;
                            }).collect(Collectors.toUnmodifiableList());

                    subScreenDTO.setSubScreenDataEntities(subScreenDataDTOS);
                    return subScreenDTO;
                }).collect(Collectors.toUnmodifiableList());

        AcademicYearDTO savedDTO = modelMapper.map(updatedEntity, AcademicYearDTO.class);

        savedDTO.setSubScreenDTOS(subScreenDTOS);

        log.info("Exit from updateAcademicYear");

        return savedDTO;
    }

    @Override
    public AcademicYearDTO getAcademicYearById(Long academicYearId) {
        log.info("Enter into getAcademicYearById");

        AcademicYearEntity academicYearEntity = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));

        AcademicYearDTO academicYearDTO = modelMapper.map(academicYearEntity, AcademicYearDTO.class);

        List<SubScreenDTO> subScreenDTOS = academicYearEntity.getSubScreenEntities().stream()
                .map(s -> {
                    SubScreenDTO subScreenDTO = modelMapper.map(s, SubScreenDTO.class);
                    subScreenDTO.setAcademicYearId(academicYearEntity.getAcademicYearId());

                    List<SubScreenDataDTO> subScreenDataDTOS = s.getSubScreenDataEntities().stream()
                            .map(sd ->{
                                SubScreenDataDTO subScreenDataDTO = modelMapper.map(sd, SubScreenDataDTO.class);
                                subScreenDataDTO.setSubScreenId(s.getSubScreenId());
                                return subScreenDataDTO;
                            }).collect(Collectors.toUnmodifiableList());

                    subScreenDTO.setSubScreenDataEntities(subScreenDataDTOS);
                    return subScreenDTO;
                }).collect(Collectors.toUnmodifiableList());

        academicYearDTO.setSubScreenDTOS(subScreenDTOS);

        log.info("Exit from getAcademicYearById");
        return academicYearDTO;
    }

    @Override
    public String deleteAcademicYear(Long academicYearId) {
        log.info("Enter into deleteAcademicYear");

        AcademicYearEntity academicYearEntity = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));

        // validations

        academicYearRepository.delete(academicYearEntity);

        log.info("Enter into deleteAcademicYear");
        return "Academic year deleted successfully";
    }

    @Override
    public Map<String, Object> getAllAcademicYearsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllAcademicYearsByFilter");

        List<AcademicYearEntity> academicYearEntities;
        Page<AcademicYearEntity> academicYearEntityPage;
        long totalElements;

        CustomQuerySpecification<AcademicYearEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if(paginate){
            academicYearEntityPage = academicYearRepository.findAll(customQuerySpecification, pageable);
            academicYearEntities = academicYearEntityPage.getContent();
            totalElements = academicYearEntityPage.getTotalElements();
        }else{
            academicYearEntities = academicYearRepository.findAll(customQuerySpecification);
            totalElements = academicYearEntities.size();
        }

        List<AcademicYearDTO> academicYearDTOS = academicYearEntities.stream()
                        .map(a -> {
                            AcademicYearDTO yearDTO = modelMapper.map(a, AcademicYearDTO.class);

                            List<SubScreenDTO> subScreenDTOS = a.getSubScreenEntities().stream()
                                    .map(s -> {
                                        SubScreenDTO subScreenDTO = modelMapper.map(s, SubScreenDTO.class);
                                        subScreenDTO.setAcademicYearId(a.getAcademicYearId());

                                        List<SubScreenDataDTO> subScreenDataDTOS = s.getSubScreenDataEntities().stream()
                                                .map(sd ->{
                                                    SubScreenDataDTO subScreenDataDTO = modelMapper.map(sd, SubScreenDataDTO.class);
                                                    subScreenDataDTO.setSubScreenId(s.getSubScreenId());
                                                    return subScreenDataDTO;
                                                }).collect(Collectors.toUnmodifiableList());

                                        subScreenDTO.setSubScreenDataEntities(subScreenDataDTOS);
                                        return subScreenDTO;
                                    }).collect(Collectors.toUnmodifiableList());

                            yearDTO.setSubScreenDTOS(subScreenDTOS);

                            return yearDTO;
                        }).collect(Collectors.toUnmodifiableList());

        log.info("Exit from getAllAcademicYearsByFilter");
        Map<String, Object> result = new HashMap<>();
        result.put("Date", academicYearDTOS);
        result.put("Total", totalElements);
        return result;
    }

    @Override
    public AcademicYearDTO getCurrentAcademicYear(){
        return null;
    }

    @Override
    public AcademicYearDTO setCurrentAcademicYear(Long academicYearId){
        return null;
    }
}
