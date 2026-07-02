package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassRoomDTO;
import com.flint.sample_be_springboot.entity.ClassRoomEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.ClassRoomRepository;
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

@Slf4j
@Service
public class ClassRoomServiceImpl implements ClassRoomService{

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

        ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);

        ClassRoomDTO savedDTO = modelMapper.map(savedEntity, ClassRoomDTO.class);

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

        modelMapper.map(classRoomDTO, existingClassRoom);

        ClassRoomEntity updatedEntity = classRoomRepository.save(existingClassRoom);

        ClassRoomDTO updatedDTO = modelMapper.map(updatedEntity, ClassRoomDTO.class);

        log.info("Exit from updateClassRoom");

        return updatedDTO;
    }

    @Override
    public ClassRoomDTO getClassRoomById(Long classRoomId) {

        log.info("Enter into getClassRoomById");

        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new CustomException("Class Room not found", HttpStatus.PRECONDITION_FAILED));

        ClassRoomDTO classRoomDTO = modelMapper.map(classRoomEntity, ClassRoomDTO.class);

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
                .map(entity -> modelMapper.map(entity, ClassRoomDTO.class))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("Data", classRoomDTOS);
        result.put("Total", totalElements);

        log.info("Exit from getAllClassRoomsByFilter");

        return result;
    }

}
