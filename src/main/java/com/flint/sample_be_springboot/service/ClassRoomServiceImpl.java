package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassRoomDTO;
import com.flint.sample_be_springboot.dto.SubjectDTO;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.ClassRoomEntity;
import com.flint.sample_be_springboot.entity.SubjectEntity;
import com.flint.sample_be_springboot.exception.CustomException;
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

        ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);
        List<SubjectDTO> subjectDTOList = savedEntity.getSubjects().stream()
                .map(s -> {
                    SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                    subjectDTO.setClassRoomId(savedEntity.getClassRoomId());

                    return subjectDTO;
                }).collect(Collectors.toUnmodifiableList());

        ClassRoomDTO savedDTO = modelMapper.map(savedEntity, ClassRoomDTO.class);

        savedDTO.setSubjectDTOList(subjectDTOList);

        log.info("Exit from saveClassRoom");

        return savedDTO;
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

        ClassRoomEntity updatedEntity = classRoomRepository.save(existingClassRoom);

        List<SubjectDTO> subjectDTOList = updatedEntity.getSubjects().stream()
                .map(s -> {
                    SubjectDTO subjectDTO = modelMapper.map(s, SubjectDTO.class);
                    subjectDTO.setClassRoomId(updatedEntity.getClassRoomId());

                    return subjectDTO;
                }).collect(Collectors.toUnmodifiableList());

        ClassRoomDTO updatedDTO = modelMapper.map(updatedEntity, ClassRoomDTO.class);

        updatedDTO.setSubjectDTOList(subjectDTOList);

        log.info("Exit from updateClassRoom");

        return updatedDTO;
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

        ClassRoomDTO classRoomDTO = modelMapper.map(classRoomEntity, ClassRoomDTO.class);
        classRoomDTO.setSubjectDTOList(subjectDTOList);

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

                    dto.setSubjectDTOList(subjectDTOList);

                    return dto;
                }).collect(Collectors.toUnmodifiableList());

        Map<String, Object> result = new HashMap<>();
        result.put("Data", classRoomDTOS);
        result.put("Total", totalElements);

        log.info("Exit from getAllClassRoomsByFilter");

        return result;
    }

}
