package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.dto.ClassMasterSearchDTO;
import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.entity.TeacherClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.ClassMasterRepository;
import com.flint.sample_be_springboot.repository.ClassSubjectAllocationRepository;
import com.flint.sample_be_springboot.repository.TeacherClassSubjectAllocationRepository;
import com.flint.sample_be_springboot.repository.TimeTableRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassMasterServiceImpl extends BaseService implements ClassMasterService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    ClassMasterRepository classMasterRepository;

    @Autowired
    private ClassSubjectAllocationRepository classSubjectAllocationRepository;

    @Autowired
    private TeacherClassSubjectAllocationRepository teacherClassSubjectAllocationRepository;

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Override
    public ClassMasterDTO getClassMasterById(Long classMasterId) {
        log.info("Enter into getClassMasterById");

        ClassMasterEntity existingClass = classMasterRepository.findById(classMasterId)
                .orElseThrow(() -> new CustomException("Class information not found", HttpStatus.NOT_FOUND));

        ClassMasterDTO classMasterDTO = modelMapper.map(existingClass, ClassMasterDTO.class);

        log.info("Exit from getClassMasterById");
        return classMasterDTO;
    }

    @Override
    public ClassMasterDTO saveClassMaster(ClassMasterDTO classMasterDTO) {
        log.info("Enter into saveClassMaster");

        if (classMasterRepository.findByStandardAndDivisionAndMedium(
                classMasterDTO.getStandard(),
                classMasterDTO.getDivision(),
                classMasterDTO.getMedium()).isPresent()) {

            throw new CustomException("Class already exists", HttpStatus.CONFLICT);
        }

        ClassMasterEntity classMasterEntity =
                modelMapper.map(classMasterDTO, ClassMasterEntity.class);

        ClassMasterEntity savedEntity = classMasterRepository.save(classMasterEntity);

        log.info("Exit from saveClassMaster");

        return modelMapper.map(savedEntity, ClassMasterDTO.class);
    }

    @Override
    public ClassMasterDTO updateClassMaster(ClassMasterDTO classMasterDTO) {
        log.info("Enter into updateClassMaster");

        ClassMasterEntity existingClass = classMasterRepository.findById(classMasterDTO.getClassMasterId())
                .orElseThrow(() ->
                        new CustomException("Class information not found", HttpStatus.NOT_FOUND));

        if (classMasterRepository.findByStandardAndDivisionAndMediumAndClassMasterIdNot(
                classMasterDTO.getStandard(),
                classMasterDTO.getDivision(),
                classMasterDTO.getMedium(),
                classMasterDTO.getClassMasterId()).isPresent()) {

            throw new CustomException("Class already exists", HttpStatus.CONFLICT);
        }

        modelMapper.map(classMasterDTO, existingClass);

        ClassMasterEntity updatedEntity = classMasterRepository.save(existingClass);

        log.info("Exit from updateClassMaster");

        return modelMapper.map(updatedEntity, ClassMasterDTO.class);
    }

    @Override
    public String deleteClassMaster(Long classMasterId) {
        log.info("Enter into deleteClassMaster");

        ClassMasterEntity existingClass = classMasterRepository.findById(classMasterId)
                .orElseThrow(() -> new CustomException("Class information not found", HttpStatus.NOT_FOUND));

        List<ClassSubjectAllocationEntity> classSubjectAllocationEntities = classSubjectAllocationRepository
                .findByClassMasterEntity_ClassMasterId(existingClass.getClassMasterId());
        if (classSubjectAllocationEntities != null && !classSubjectAllocationEntities.isEmpty()) {
            throw new CustomException("This Class master is assigned in subject assignment and teacher subjects, can't delete this class",
                    HttpStatus.FOUND);
        }

        List<TeacherClassSubjectAllocationEntity> teacherClassSubjectAllocationEntities = teacherClassSubjectAllocationRepository
                .findByClassMasterEntity_ClassMasterId(existingClass.getClassMasterId());
        if (teacherClassSubjectAllocationEntities != null && !teacherClassSubjectAllocationEntities.isEmpty()) {
            throw new CustomException("This Class is assigned in subject assignment and teacher subjects, can't delete this class",
                    HttpStatus.FOUND);
        }


        classMasterRepository.delete(existingClass);

        log.info("Exit from deleteClassMaster");

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllClassMasterByFilter(Map<String, Object> filter,
                                                         Pageable pageable, boolean paginate) {

        log.info("Enter into getAllClassMasterByFilter");

        Page<ClassMasterEntity> classMasterPage;
        List<ClassMasterEntity> classMasterEntityList;
        long totalElements;

        CustomQuerySpecification<ClassMasterEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            classMasterPage = classMasterRepository.findAll(customQuerySpecification, pageable);
            classMasterEntityList = classMasterPage.getContent();
            totalElements = classMasterPage.getTotalElements();
        } else {
            classMasterEntityList = classMasterRepository.findAll(customQuerySpecification);
            totalElements = classMasterEntityList.size();
        }

        List<ClassMasterDTO> classMasterDTOS = classMasterEntityList.stream()
                .map(entity -> modelMapper.map(entity, ClassMasterDTO.class)).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("classMasterDTOS", classMasterDTOS);
        result.put("total element", totalElements);

        log.info("Exit from getAllClassMasterByFilter");

        return result;
    }

    @Override
    public List<ClassMasterSearchDTO> searchClasses(String keyword) {

        log.info("Enter into searchClasses");

        List<ClassMasterEntity> classMasterEntities =
                classMasterRepository.searchClasses(keyword);

        List<ClassMasterSearchDTO> result = classMasterEntities.stream()
                .map(entity -> {

                    ClassMasterSearchDTO dto = new ClassMasterSearchDTO();

                    dto.setClassMasterId(entity.getClassMasterId());

                    dto.setDisplayName(
                            entity.getStandard()
                                    + " - "
                                    + entity.getDivision()
                                    + " ("
                                    + entity.getMedium()
                                    + ")"
                    );

                    return dto;
                })
                .toList();

        log.info("Exit from searchClasses");

        return result;
    }

}
