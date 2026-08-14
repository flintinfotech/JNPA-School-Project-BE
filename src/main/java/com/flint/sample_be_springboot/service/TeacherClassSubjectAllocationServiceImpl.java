package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.dto.TeacherClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import com.flint.sample_be_springboot.entity.TeacherClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.*;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeacherClassSubjectAllocationServiceImpl extends BaseService implements TeacherClassSubjectAllocationService {

    @Autowired
    private TeacherClassSubjectAllocationRepository teacherClassSubjectAllocationRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private ClassMasterRepository classMasterRepository;

    @Autowired
    private ClassSubjectAllocationRepository classSubjectAllocationRepository;

    @Autowired
    private SubjectMasterRepository subjectMasterRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public String updateTeacherClassSubjectAllocation(TeacherClassSubjectAllocationDTO dto) {

        log.info("Enter into updateTeacherClassSubjectAllocation");

        EmployeeDetailsEntity user = employeeDetailsRepository.findById(dto.getEmployeeDetailsId())
                .orElseThrow(() ->
                        new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

        ClassMasterEntity classEntity = classMasterRepository.findById(dto.getClassMasterId())
                .orElseThrow(() ->
                        new CustomException("Class not found", HttpStatus.NOT_FOUND));

        // Existing allocations ONLY for this teacher & this class
        List<TeacherClassSubjectAllocationEntity> existing =
                teacherClassSubjectAllocationRepository
                        .findByEmployeeDetailsIdAndClassMasterIdAndAcademicYear(
                                dto.getEmployeeDetailsId(),
                                dto.getClassMasterId(),
                                getStartDate(),
                                getEndDate());

        log.info("Existing Allocations:");
        existing.forEach(e -> log.info(
                "Class : {}, Subject : {}",
                e.getClassMasterEntity().getClassMasterId(),
                e.getSubjectMasterEntity().getSubjectMasterId()
        ));

        Set<Long> existingSubjectIds = existing.stream()
                .map(e -> e.getSubjectMasterEntity().getSubjectMasterId())
                .collect(Collectors.toSet());

        Set<Long> requestedSubjectIds = new HashSet<>(dto.getSubjectIds());

        // Delete removed subjects
        List<TeacherClassSubjectAllocationEntity> toDelete = existing.stream()
                .filter(e -> !requestedSubjectIds.contains(
                        e.getSubjectMasterEntity().getSubjectMasterId()))
                .collect(Collectors.toList());

        log.info("Deleting {} allocations", toDelete.size());

        teacherClassSubjectAllocationRepository.deleteAll(toDelete);

        // Add newly selected subjects
        for (Long subjectId : requestedSubjectIds) {

            if (existingSubjectIds.contains(subjectId)) {
                continue;
            }

            SubjectMasterEntity subject = subjectMasterRepository.findById(subjectId)
                    .orElseThrow(() ->
                            new CustomException("Subject not found", HttpStatus.NOT_FOUND));

            // Validate subject belongs to selected class
            boolean assigned = classSubjectAllocationRepository
                    .existsByClassMasterIdAndSubjectMasterIdAndAcademicYear(
                            dto.getClassMasterId(),
                            subjectId,
                            getStartDate(),
                            getEndDate());

            if (!assigned) {
                throw new CustomException(
                        "Selected subject is not assigned to selected class",
                        HttpStatus.BAD_REQUEST
                );
            }

            TeacherClassSubjectAllocationEntity allocation =
                    new TeacherClassSubjectAllocationEntity();

            allocation.setEmployeeDetailsEntity(user);
            allocation.setClassMasterEntity(classEntity);
            allocation.setSubjectMasterEntity(subject);
            allocation.setStartDate(getStartDate());
            allocation.setEndDate(getEndDate());

            teacherClassSubjectAllocationRepository.save(allocation);

            log.info("Saved -> Class : {}, Subject : {}",
                    dto.getClassMasterId(),
                    subjectId);
        }

        log.info("Exit from updateTeacherClassSubjectAllocation");

        return "Teacher class subject allocation updated successfully";
    }

    @Override
    public Map<ClassMasterDTO, List<SubjectMasterDTO>> getTeacherClassSubjectAllocation(Long userInformationId) {
        log.info("Enter into getTeacherClassSubjectAllocation");

        Map<ClassMasterDTO, List<SubjectMasterDTO>> map;

        List<TeacherClassSubjectAllocationEntity> entities = teacherClassSubjectAllocationRepository.
                findByEmployeeDetailsIdAndAcademicYear
                (userInformationId, getStartDate(), getEndDate());

        map = entities.stream()
                .collect(Collectors.groupingBy(
                        entity -> modelMapper.map(entity.getClassMasterEntity(), ClassMasterDTO.class),
                        Collectors.mapping(
                                entity -> modelMapper.map(entity.getSubjectMasterEntity(), SubjectMasterDTO.class),
                                Collectors.toList()
                        )
                ));

        log.info("Exit from getTeacherClassSubjectAllocation");
        return map;
    }
}
