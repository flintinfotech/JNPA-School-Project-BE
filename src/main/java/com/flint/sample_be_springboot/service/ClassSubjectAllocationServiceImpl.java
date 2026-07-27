package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.ClassMasterRepository;
import com.flint.sample_be_springboot.repository.ClassSubjectAllocationRepository;
import com.flint.sample_be_springboot.repository.SubjectMasterRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassSubjectAllocationServiceImpl extends BaseService implements ClassSubjectAllocationService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ClassSubjectAllocationRepository classSubjectAllocationRepository;

    @Autowired
    private ClassMasterRepository classMasterRepository;

    @Autowired
    private SubjectMasterRepository subjectMasterRepository;

    @Override
    public String updateClassSubjects(ClassSubjectAllocationDTO dto) {

        ClassMasterEntity classEntity = classMasterRepository.findById(dto.getClassMasterId())
                .orElseThrow(() -> new CustomException("Class not found", HttpStatus.NOT_FOUND));

        List<ClassSubjectAllocationEntity> existing =
                classSubjectAllocationRepository.findByClassMasterEntity_ClassMasterId(dto.getClassMasterId());

        Set<Long> existingIds = existing.stream()
                .map(e -> e.getSubjectMasterEntity().getSubjectMasterId())
                .collect(Collectors.toSet());

        Set<Long> requestedIds = new HashSet<>(dto.getSubjectMasterIds());

        // Delete only removed subjects
        List<ClassSubjectAllocationEntity> toDelete = existing.stream()
                .filter(e -> !requestedIds.contains(
                        e.getSubjectMasterEntity().getSubjectMasterId()))
                .toList();

        classSubjectAllocationRepository.deleteAll(toDelete);

        // Add only newly selected subjects
        for (Long subjectId : requestedIds) {

            if (existingIds.contains(subjectId)) {
                continue; // already assigned
            }

            SubjectMasterEntity subject = subjectMasterRepository.findById(subjectId)
                    .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

            ClassSubjectAllocationEntity allocation = new ClassSubjectAllocationEntity();
            allocation.setClassMasterEntity(classEntity);
            allocation.setSubjectMasterEntity(subject);

            classSubjectAllocationRepository.save(allocation);
        }

        return "Subject assigned to class successfully";
    }

    @Override
    public List<SubjectMasterDTO> getSubjectsByClass(Long classId) {

        List<ClassSubjectAllocationEntity> allocations =
                classSubjectAllocationRepository.findByClassMasterEntity_ClassMasterId(classId);

        return allocations.stream()
                .map(a -> {

                    SubjectMasterEntity subject = a.getSubjectMasterEntity();

                    SubjectMasterDTO dto = new SubjectMasterDTO();

                    dto.setSubjectMasterId(subject.getSubjectMasterId());
                    dto.setSubjectName(subject.getSubjectName());
                    dto.setSubjectCode(subject.getSubjectCode());

                    return dto;

                }).toList();
    }

}
