package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.*;
import com.flint.sample_be_springboot.entity.*;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.AcademicYearRepository;
import com.flint.sample_be_springboot.repository.ClassRoomRepository;
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
public class ClassRoomServiceImpl extends BaseService implements ClassRoomService{

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Override
    public ClassRoomDTO saveClassRoom(ClassRoomDTO classRoomDTO) {

        log.info("Enter into saveClassRoom");

        ClassRoomEntity existingClassRoom = classRoomRepository.findByClassRoomName(classRoomDTO.getClassRoomName());

        if (existingClassRoom != null) {
            throw new CustomException("Class Room already exists", HttpStatus.PRECONDITION_FAILED);
        }

        // Other validations

        ClassRoomEntity classRoomEntity = modelMapper.map(classRoomDTO, ClassRoomEntity.class);
        classRoomEntity.setAuditDetails(addAuditDetails(classRoomEntity.getAuditDetails()));

        List<SubjectEntity> subjectEntities = new ArrayList<>();
        if(classRoomDTO.getSubjectDTOList() != null && !classRoomDTO.getSubjectDTOList().isEmpty()){
            for(SubjectDTO dto : classRoomDTO.getSubjectDTOList()){
                SubjectEntity subjectEntity = modelMapper.map(dto, SubjectEntity.class);
                subjectEntity.setClassRoomEntity(classRoomEntity);
                subjectEntities.add(subjectEntity);
            }
        }

        if(classRoomDTO.getBrochure() != null){
            classRoomEntity.setBrochure(Base64.getDecoder().decode(classRoomDTO.getBrochure()));
        }

        classRoomEntity.setSubjects(subjectEntities);

        List<AcademicYearEntity> academicYearEntities = new ArrayList<>();
        if(classRoomDTO.getAcademicYearDTOS() != null){
            for(AcademicYearDTO yearDTO : classRoomDTO.getAcademicYearDTOS()){
                AcademicYearEntity academicYearEntity = setAcademicYear(yearDTO);
                academicYearEntity.setClassRoomEntity(classRoomEntity);
                academicYearEntities.add(academicYearEntity);
            }
        }

        classRoomEntity.setAcademicYearEntities(academicYearEntities);

        ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);
        List<SubjectDTO> subjectDTOList = savedEntity.getSubjects().stream()
                .map(s -> {
                    SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                    subjectDTO.setClassRoomId(savedEntity.getClassRoomId());

                    return subjectDTO;
                }).collect(Collectors.toUnmodifiableList());

        List<AcademicYearDTO> academicYearDTOS = savedEntity.getAcademicYearEntities().stream()
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

        ClassRoomDTO savedDTO = modelMapper.map(savedEntity, ClassRoomDTO.class);

        savedDTO.setSubjectDTOList(subjectDTOList);
        savedDTO.setAcademicYearDTOS(academicYearDTOS);

        log.info("Exit from saveClassRoom");

        return savedDTO;
    }

    private AcademicYearEntity setAcademicYear(AcademicYearDTO academicYearDTO) {

        AcademicYearEntity existingAcademicYear = academicYearRepository
                .findByAcademicYearName(academicYearDTO.getAcademicYearName());

        if (existingAcademicYear != null) {
            throw new CustomException("This year is already present", HttpStatus.PRECONDITION_FAILED);
        }

        AcademicYearEntity academicYearEntity = modelMapper.map(academicYearDTO, AcademicYearEntity.class);
        academicYearEntity.setAuditDetails(addAuditDetails(academicYearEntity.getAuditDetails()));

        List<SubScreenEntity> subScreenEntities = new ArrayList<>();

        if (academicYearDTO.getSubScreenDTOS() != null && !academicYearDTO.getSubScreenDTOS().isEmpty()) {

            for (SubScreenDTO subScreenDTO : academicYearDTO.getSubScreenDTOS()) {

                SubScreenEntity subScreenEntity = new SubScreenEntity();
                subScreenEntity.setSubScreenName(subScreenDTO.getSubScreenName());

                // Set parent reference
                subScreenEntity.setAcademicYearEntity(academicYearEntity);

                List<SubScreenDataEntity> subScreenDataEntities = new ArrayList<>();

                if (subScreenDTO.getSubScreenDataEntities() != null
                        && !subScreenDTO.getSubScreenDataEntities().isEmpty()) {

                    for (SubScreenDataDTO dataDTO : subScreenDTO.getSubScreenDataEntities()) {

                        SubScreenDataEntity dataEntity = new SubScreenDataEntity();

                        dataEntity.setSubjectName(dataDTO.getSubjectName());

                        if (dataDTO.getSubjectData() != null && !dataDTO.getSubjectData().isBlank()) {
                            dataEntity.setSubjectData(Base64.getDecoder().decode(dataDTO.getSubjectData()));
                        }

                        // Set parent reference
                        dataEntity.setSubScreenEntity(subScreenEntity);

                        subScreenDataEntities.add(dataEntity);
                    }
                }

                subScreenEntity.setSubScreenDataEntities(subScreenDataEntities);
                subScreenEntities.add(subScreenEntity);
            }
        }

        academicYearEntity.setSubScreenEntities(subScreenEntities);

        return academicYearEntity;
    }

    @Override
    public ClassRoomDTO updateClassRoom(ClassRoomDTO classRoomDTO) {

        log.info("Enter into updateClassRoom");

        ClassRoomEntity existingClassRoom = classRoomRepository.findById(classRoomDTO.getClassRoomId())
                .orElseThrow(() -> new CustomException("Class Room not found", HttpStatus.PRECONDITION_FAILED));

        classRoomRepository
                .findByClassRoomNameAndClassRoomIdNot(
                        classRoomDTO.getClassRoomName(),
                        existingClassRoom.getClassRoomId())
                .ifPresent(entity -> {
                    throw new CustomException("Class Room already exists", HttpStatus.PRECONDITION_FAILED);
                });

        AuditDetails auditDetails = existingClassRoom.getAuditDetails();

        modelMapper.map(classRoomDTO, existingClassRoom);
        existingClassRoom.setAuditDetails(addAuditDetails(auditDetails));

        if(classRoomDTO.getBrochure() != null){
            existingClassRoom.setBrochure(Base64.getDecoder().decode(classRoomDTO.getBrochure()));
        }else{
            existingClassRoom.setBrochure(null);
        }

        // Delete removed subjects
        Set<Long> requestSubjectIds = classRoomDTO.getSubjectDTOList().stream()
                .map(SubjectDTO::getSubjectId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingClassRoom.getSubjects().removeIf(subject ->
                subject.getSubjectId() != null &&
                        !requestSubjectIds.contains(subject.getSubjectId()));

        // Existing subjects map
        Map<Long, SubjectEntity> existingSubjects =
                existingClassRoom.getSubjects().stream()
                        .collect(Collectors.toMap(
                                SubjectEntity::getSubjectId,
                                Function.identity()));

        for (SubjectDTO subjectDTO : classRoomDTO.getSubjectDTOList()) {

            SubjectEntity subjectEntity;

            if (subjectDTO.getSubjectId() != null &&
                    existingSubjects.containsKey(subjectDTO.getSubjectId())) {

                // Update existing subject
                subjectEntity = existingSubjects.get(subjectDTO.getSubjectId());

            } else {

                // Create new subject
                subjectEntity = new SubjectEntity();
                subjectEntity.setClassRoomEntity(existingClassRoom);

                existingClassRoom.getSubjects().add(subjectEntity);
            }

            subjectEntity.setSubjectName(subjectDTO.getSubjectName());
            subjectEntity.setSubjectDescription(subjectDTO.getSubjectDescription());
        }

        // set academic year entities
// Delete removed Academic Years
        Set<Long> requestAcademicYearIds = classRoomDTO.getAcademicYearDTOS().stream()
                .map(AcademicYearDTO::getAcademicYearId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingClassRoom.getAcademicYearEntities().removeIf(academicYear ->
                academicYear.getAcademicYearId() != null &&
                        !requestAcademicYearIds.contains(academicYear.getAcademicYearId()));

// Existing Academic Year map
        Map<Long, AcademicYearEntity> existingAcademicYears =
                existingClassRoom.getAcademicYearEntities().stream()
                        .collect(Collectors.toMap(
                                AcademicYearEntity::getAcademicYearId,
                                Function.identity()));

        for (AcademicYearDTO academicYearDTO : classRoomDTO.getAcademicYearDTOS()) {

            AcademicYearEntity academicYearEntity;

            if (academicYearDTO.getAcademicYearId() != null &&
                    existingAcademicYears.containsKey(academicYearDTO.getAcademicYearId())) {

                // Update existing Academic Year
                updateAcademicYear(existingAcademicYears.get(academicYearDTO.getAcademicYearId()),academicYearDTO);

            } else {

                // Create new Academic Year
                academicYearEntity = setAcademicYear(academicYearDTO);
                academicYearEntity.setClassRoomEntity(existingClassRoom);

                existingClassRoom.getAcademicYearEntities().add(academicYearEntity);
            }
        }


        ClassRoomEntity updatedEntity = classRoomRepository.save(existingClassRoom);

        List<SubjectDTO> subjectDTOList = updatedEntity.getSubjects().stream()
                .map(s -> {
                    SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                    subjectDTO.setClassRoomId(updatedEntity.getClassRoomId());

                    return subjectDTO;
                }).collect(Collectors.toUnmodifiableList());

        List<AcademicYearDTO> academicYearDTOS = updatedEntity.getAcademicYearEntities().stream()
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

        ClassRoomDTO updatedDTO = modelMapper.map(updatedEntity, ClassRoomDTO.class);

        updatedDTO.setSubjectDTOList(subjectDTOList);
        updatedDTO.setAcademicYearDTOS(academicYearDTOS);

        log.info("Exit from updateClassRoom");

        return updatedDTO;
    }

    private void updateAcademicYear(AcademicYearEntity existingEntity,
                                    AcademicYearDTO academicYearDTO) {

        // Duplicate validation
        academicYearRepository
                .findByAcademicYearNameAndAcademicYearIdNot(
                        academicYearDTO.getAcademicYearName(),
                        existingEntity.getAcademicYearId())
                .ifPresent(entity -> {
                    throw new CustomException(
                            "This academic year already exists",
                            HttpStatus.PRECONDITION_FAILED);
                });

        AuditDetails auditDetails = existingEntity.getAuditDetails();

        modelMapper.map(academicYearDTO, existingEntity);

        existingEntity.setAuditDetails(addAuditDetails(auditDetails));

        List<SubScreenDTO> requestSubScreens =
                Optional.ofNullable(academicYearDTO.getSubScreenDTOS())
                        .orElse(Collections.emptyList());

        // Remove deleted Sub Screens
        Set<Long> requestSubScreenIds = requestSubScreens.stream()
                .map(SubScreenDTO::getSubScreenId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getSubScreenEntities().removeIf(subScreen ->
                subScreen.getSubScreenId() != null &&
                        !requestSubScreenIds.contains(subScreen.getSubScreenId()));

        // Existing SubScreen map
        Map<Long, SubScreenEntity> existingSubScreens =
                existingEntity.getSubScreenEntities().stream()
                        .filter(s -> s.getSubScreenId() != null)
                        .collect(Collectors.toMap(
                                SubScreenEntity::getSubScreenId,
                                Function.identity()));

        for (SubScreenDTO subScreenDTO : requestSubScreens) {

            SubScreenEntity subScreenEntity;

            if (subScreenDTO.getSubScreenId() != null &&
                    existingSubScreens.containsKey(subScreenDTO.getSubScreenId())) {

                // Update existing
                subScreenEntity = existingSubScreens.get(subScreenDTO.getSubScreenId());

            } else {

                // Create new
                subScreenEntity = new SubScreenEntity();
                subScreenEntity.setAcademicYearEntity(existingEntity);

                subScreenEntity.setSubScreenDataEntities(new ArrayList<>());

                existingEntity.getSubScreenEntities().add(subScreenEntity);
            }

            subScreenEntity.setSubScreenName(subScreenDTO.getSubScreenName());

            List<SubScreenDataDTO> requestData =
                    Optional.ofNullable(subScreenDTO.getSubScreenDataEntities())
                            .orElse(Collections.emptyList());

            // Remove deleted data
            Set<Long> requestDataIds = requestData.stream()
                    .map(SubScreenDataDTO::getSubScreenDataId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            subScreenEntity.getSubScreenDataEntities().removeIf(data ->
                    data.getSubScreenDataId() != null &&
                            !requestDataIds.contains(data.getSubScreenDataId()));

            // Existing data map
            Map<Long, SubScreenDataEntity> existingData =
                    subScreenEntity.getSubScreenDataEntities().stream()
                            .filter(d -> d.getSubScreenDataId() != null)
                            .collect(Collectors.toMap(
                                    SubScreenDataEntity::getSubScreenDataId,
                                    Function.identity()));

            for (SubScreenDataDTO dataDTO : requestData) {

                SubScreenDataEntity dataEntity;

                if (dataDTO.getSubScreenDataId() != null &&
                        existingData.containsKey(dataDTO.getSubScreenDataId())) {

                    // Update existing
                    dataEntity = existingData.get(dataDTO.getSubScreenDataId());

                } else {

                    // Create new
                    dataEntity = new SubScreenDataEntity();
                    dataEntity.setSubScreenEntity(subScreenEntity);

                    subScreenEntity.getSubScreenDataEntities().add(dataEntity);
                }

                dataEntity.setSubjectName(dataDTO.getSubjectName());

                if (dataDTO.getSubjectData() != null &&
                        !dataDTO.getSubjectData().isBlank()) {

                    dataEntity.setSubjectData(
                            Base64.getDecoder().decode(dataDTO.getSubjectData()));

                } else {

                    dataEntity.setSubjectData(null);
                }
            }
        }
    }

    @Override
    public ClassRoomDTO getClassRoomById(Long classRoomId) {

        log.info("Enter into getClassRoomById");

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new CustomException("Class Room not found", HttpStatus.PRECONDITION_FAILED));

        List<SubjectDTO> subjectDTOList = classRoomEntity.getSubjects().stream()
                .map(s -> {
                    SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                    subjectDTO.setClassRoomId(classRoomEntity.getClassRoomId());

                    return subjectDTO;
                }).collect(Collectors.toUnmodifiableList());

        List<AcademicYearDTO> academicYearDTOS = classRoomEntity.getAcademicYearEntities().stream()
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

        ClassRoomDTO classRoomDTO = modelMapper.map(classRoomEntity, ClassRoomDTO.class);
        classRoomDTO.setSubjectDTOList(subjectDTOList);
        classRoomDTO.setAcademicYearDTOS(academicYearDTOS);

        log.info("Exit from getClassRoomById");

        return classRoomDTO;
    }

    @Override
    public String deleteClassRoom(Long classRoomId) {

        log.info("Enter into deleteClassRoom");

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new CustomException("Class Room not found", HttpStatus.PRECONDITION_FAILED));

        // Other validations

        classRoomRepository.delete(classRoomEntity);

        log.info("Exit from deleteClassRoom");

        return "Class Room deleted successfully";
    }

    @Override
    public Map<String, Object> getAllClassRoomsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {

        log.info("Enter into getAllClassRoomsByFilter");

        List<ClassRoomEntity> classRoomEntities;
        Page<ClassRoomEntity> classRoomEntityPage;
        long totalElements;

        CustomQuerySpecification<ClassRoomEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        if (paginate) {

            classRoomEntityPage = classRoomRepository.findAll(customQuerySpecification, pageable);

            classRoomEntities = classRoomEntityPage.getContent();

            totalElements = classRoomEntityPage.getTotalElements();

        } else {

            classRoomEntities = classRoomRepository.findAll(customQuerySpecification);

            totalElements = classRoomEntities.size();
        }

        List<ClassRoomDTO> classRoomDTOS = classRoomEntities.stream()
                .map(entity -> {
                    ClassRoomDTO dto = modelMapper.map(entity, ClassRoomDTO.class);

                    List<SubjectDTO> subjectDTOList = entity.getSubjects().stream()
                            .map(s -> {
                                SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                                subjectDTO.setClassRoomId(entity.getClassRoomId());

                                return subjectDTO;
                            }).collect(Collectors.toUnmodifiableList());

                    List<AcademicYearDTO> academicYearDTOS = entity.getAcademicYearEntities().stream()
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

                    dto.setSubjectDTOList(subjectDTOList);
                    dto.setAcademicYearDTOS(academicYearDTOS);

                    return dto;
                }).collect(Collectors.toUnmodifiableList());

        Map<String, Object> result = new HashMap<>();
        result.put("Data", classRoomDTOS);
        result.put("Total", totalElements);

        log.info("Exit from getAllClassRoomsByFilter");

        return result;
    }

}
