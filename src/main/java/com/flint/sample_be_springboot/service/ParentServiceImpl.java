package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.ParentDTO;
import com.flint.sample_be_springboot.entity.student.ParentEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.student.ParentRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
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
public class ParentServiceImpl extends BaseService implements ParentService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ParentDTO getParentById(Long parentId) {

        ParentEntity existingEntity = parentRepository.findById(parentId)
                .orElseThrow(() ->
                        new CustomException("Parent information not found", HttpStatus.NOT_FOUND));

        ParentDTO parentDTO = modelMapper.map(existingEntity, ParentDTO.class);
        return parentDTO;
    }

    @Override
    public ParentDTO saveParent(ParentDTO parentDTO) {
        if (parentDTO == null) {
            throw new CustomException("Parent information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

//        Optional<ParentEntity> parentEntity = parentRepository.findById(parentDTO.getParentId());
//        if (parentEntity.isPresent()) {
//            throw new CustomException("Parent id is already exist", HttpStatus.CONFLICT);
//        }

        ParentEntity parent = modelMapper.map(parentDTO, ParentEntity.class);
        StudentEntity studentEntity = studentRepository.findById(parentDTO.getStudentId()).get();
        parent.setStudentEntity(studentEntity);
        ParentEntity parent1 = parentRepository.save(parent);
        ParentDTO savedDTO = modelMapper.map(parent1, ParentDTO.class);

        return savedDTO;

    }
    @Override
    public ParentDTO updateParent(ParentDTO parentDTO) {

        if (parentDTO == null) {
            throw new CustomException("Parent information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (parentDTO.getParentId() == null) {
            throw new CustomException("Parent ID cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        // Find existing parent
        ParentEntity existingEntity = parentRepository.findById(parentDTO.getParentId())
                .orElseThrow(() ->
                        new CustomException("Parent information not found", HttpStatus.NOT_FOUND));

        // Update parent fields
        existingEntity.setName(parentDTO.getName());
        existingEntity.setRelation(parentDTO.getRelation());
        existingEntity.setOccupation(parentDTO.getOccupation());
        existingEntity.setPhone(parentDTO.getPhone());
        existingEntity.setEmail(parentDTO.getEmail());
        existingEntity.setAddress(parentDTO.getAddress());
        existingEntity.setAnnualIncome(parentDTO.getAnnualIncome());

        // Update audit details
        existingEntity.setAuditDetails(addAuditDetails(existingEntity.getAuditDetails()));

        // Save
        ParentEntity savedEntity = parentRepository.save(existingEntity);

        // Convert Entity -> DTO
        ParentDTO savedDTO = modelMapper.map(savedEntity, ParentDTO.class);

        return savedDTO;
    }

    @Override
    public String deleteParent(Long parentId) {

        ParentEntity existingParent = parentRepository.findById(parentId)
                .orElseThrow(() ->
                        new CustomException("Parent information not found", HttpStatus.NOT_FOUND));

        StudentEntity studentEntity = existingParent.getStudentEntity();

        if (studentEntity != null) {

            existingParent.setStudentEntity(null);
            studentEntity.setParentEntity(null);
            parentRepository.save(existingParent);
            studentRepository.delete(studentEntity);
        }

        parentRepository.delete(existingParent);

        return "Record deleted successfully";

    }

    @Override
    public Map<String, Object> getAllParentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {

        Page<ParentEntity> informationEntityPage;
        List<ParentEntity> parentEntities;
        long totalElement;

        CustomQuerySpecification<ParentEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            informationEntityPage = parentRepository.findAll(customQuerySpecification, pageable);
            parentEntities = informationEntityPage.getContent();
            totalElement = informationEntityPage.getTotalElements();
        } else {
            parentEntities = parentRepository.findAll(customQuerySpecification);
            totalElement = (long) parentEntities.size();
        }

        List<ParentDTO> parentDTOS = parentEntities.stream()
                .map(p -> modelMapper.map(p, ParentDTO.class))
                .collect(Collectors.toUnmodifiableList());

        Map<String, Object> map = new HashMap<>();
        map.put("Parent list", parentDTOS);
        map.put("total elements", totalElement);

        return map;
    }
}

