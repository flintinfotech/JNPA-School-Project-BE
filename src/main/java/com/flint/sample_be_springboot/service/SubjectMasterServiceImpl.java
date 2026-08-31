package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import com.flint.sample_be_springboot.entity.TeacherClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.ClassSubjectAllocationRepository;
import com.flint.sample_be_springboot.repository.SubjectMasterRepository;
import com.flint.sample_be_springboot.repository.TeacherClassSubjectAllocationRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectMasterServiceImpl extends BaseService implements SubjectMasterService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private SubjectMasterRepository subjectMasterRepository;

    @Autowired
    private ClassSubjectAllocationRepository classSubjectAllocationRepository;

    @Autowired
    private TeacherClassSubjectAllocationRepository teacherClassSubjectAllocationRepository;

    @Override
    public SubjectMasterDTO getSubjectMasterById(Long subjectMasterId) {
        log.info("Enter into getSubjectMasterById");

        SubjectMasterEntity existingSubject = subjectMasterRepository.findById(subjectMasterId)
                .orElseThrow(() -> new CustomException("Subject information not found", HttpStatus.NOT_FOUND));

        SubjectMasterDTO subjectMasterDTO = modelMapper.map(existingSubject, SubjectMasterDTO.class);

        log.info("Exit from getSubjectMasterById");
        return subjectMasterDTO;
    }

    @Override
    public SubjectMasterDTO saveSubjectMaster(SubjectMasterDTO subjectMasterDTO) {
        log.info("Enter into saveSubjectMaster");

        Optional<SubjectMasterEntity> existingSubject = subjectMasterRepository.findBySubjectName(subjectMasterDTO.getSubjectName());
        if (existingSubject.isPresent()) {
            throw new CustomException("Subject already exist", HttpStatus.PRECONDITION_FAILED);
        }

        SubjectMasterEntity subjectMasterEntity = modelMapper.map(subjectMasterDTO, SubjectMasterEntity.class);

        SubjectMasterEntity savedEntity = subjectMasterRepository.save(subjectMasterEntity);

        SubjectMasterDTO savedDto = modelMapper.map(savedEntity, SubjectMasterDTO.class);

        log.info("Exit from saveSubjectMaster");
        return savedDto;
    }

    @Override
    public SubjectMasterDTO updateSubjectMaster(SubjectMasterDTO subjectMasterDTO) {
        log.info("Enter into updateSubjectMaster");

        SubjectMasterEntity existingSubject = subjectMasterRepository.findById(subjectMasterDTO.getSubjectMasterId())
                .orElseThrow(() -> new CustomException("Subject information not found", HttpStatus.NOT_FOUND));

        Optional<SubjectMasterEntity> isDuplicate = subjectMasterRepository.findBySubjectNameAndSubjectMasterIdNot
                (subjectMasterDTO.getSubjectName(), subjectMasterDTO.getSubjectMasterId());
        if (isDuplicate.isPresent()) {
            throw new CustomException("Subject already exist", HttpStatus.PRECONDITION_FAILED);
        }

        modelMapper.map(subjectMasterDTO, existingSubject);

        SubjectMasterEntity updatedEntity = subjectMasterRepository.save(existingSubject);

        SubjectMasterDTO updatedDto = modelMapper.map(updatedEntity, SubjectMasterDTO.class);

        log.info("Exit from updateSubjectMaster");
        return updatedDto;
    }

    @Override
    public String deleteSubjectMaster(Long subjectMasterId) {
        log.info("Enter into deleteSubjectMaster");

        SubjectMasterEntity existingSubject = subjectMasterRepository.findById(subjectMasterId)
                .orElseThrow(() -> new CustomException("Subject information not found", HttpStatus.NOT_FOUND));

        List<ClassSubjectAllocationEntity> classSubjectAllocationEntities = classSubjectAllocationRepository
                .findBySubjectMasterEntity_SubjectMasterId(existingSubject.getSubjectMasterId());
        if (classSubjectAllocationEntities != null && !classSubjectAllocationEntities.isEmpty()) {
            throw new CustomException("This Subject is assigned in subject assignment and teacher subjects, can't delete this ", HttpStatus.FOUND);
        }

        List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = teacherClassSubjectAllocationRepository
                .findBySubjectMasterEntity_SubjectMasterId(existingSubject.getSubjectMasterId());
        if (teacherClassSubjectAllocationEntities != null && !teacherClassSubjectAllocationEntities.isEmpty()) {
            throw new CustomException("This Subject is assigned in subject assignment and teacher subjects, can't delete this user", HttpStatus.FOUND);
        }

        subjectMasterRepository.delete(existingSubject);

        log.info("Exit from deleteSubjectMaster");

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllSubjectMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllSubjectMasterByFilter");

        Page<SubjectMasterEntity> subjectMasterPage;
        List<SubjectMasterEntity> subjectMasterEntityList;
        long totalElements;

        CustomQuerySpecification<SubjectMasterEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            subjectMasterPage = subjectMasterRepository.findAll(customQuerySpecification, pageable);
            subjectMasterEntityList = subjectMasterPage.getContent();
            totalElements = subjectMasterPage.getTotalElements();
        } else {
            subjectMasterEntityList = subjectMasterRepository.findAll(customQuerySpecification);
            totalElements = subjectMasterEntityList.size();
        }

        List<SubjectMasterDTO> subjectMasterDTOS = subjectMasterEntityList.stream()
                .map(subject -> modelMapper.map(subject, SubjectMasterDTO.class))
                .collect(Collectors.toList());

        log.info("Exit from getAllSubjectMasterByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("subjectMasterDTOS", subjectMasterDTOS);
        result.put("total element", totalElements);

        return result;
    }
}
