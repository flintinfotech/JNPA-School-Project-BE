package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.*;
import com.flint.sample_be_springboot.entity.*;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.StudentRepository;
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
public class StudentServiceImpl implements StudentService{

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        log.info("Enter into saveStudent");

        if(studentDTO == null){
            throw new CustomException("Student info cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentEntity studentEntity = modelMapper.map(studentDTO, StudentEntity.class);

        // set parent entities
        List<ParentEntity> parentEntities = new ArrayList<>();
        if(studentDTO.getParentEntities() != null && !studentDTO.getParentEntities().isEmpty()){
            for(ParentDTO parentDTO : studentDTO.getParentEntities()){
                ParentEntity parentEntity = modelMapper.map(parentDTO, ParentEntity.class);
                parentEntity.setStudentEntity(studentEntity);
                parentEntities.add(parentEntity);
            }
        }
        studentEntity.setParentEntities(parentEntities);

        // set student documents
        List<StudentDocumentEntity> studentDocumentEntities = new ArrayList<>();
        if(studentDTO.getStudentDocuments() != null && !studentDTO.getStudentDocuments().isEmpty()){
            for(StudentDocumentDTO studentDocumentDTO : studentDTO.getStudentDocuments()){
                StudentDocumentEntity studentDocumentEntity = modelMapper.map(studentDocumentDTO, StudentDocumentEntity.class);
                if(studentDocumentDTO.getDocument() != null){
                    studentDocumentEntity.setDocument(Base64.getDecoder().decode(studentDocumentDTO.getDocument()));
                }
                studentDocumentEntity.setStudentEntity(studentEntity);

                studentDocumentEntities.add(studentDocumentEntity);
            }
        }
        studentEntity.setStudentDocumentEntities(studentDocumentEntities);

        // set student academic information
        List<AcademicInformationEntity> academicInformationEntities = new ArrayList<>();
        if(studentDTO.getAcademicInformation() != null && !studentDTO.getAcademicInformation().isEmpty()){
            for(AcademicInformationDTO academicInformationDTO : studentDTO.getAcademicInformation()){
                AcademicInformationEntity academicInformationEntity = modelMapper.map(academicInformationDTO, AcademicInformationEntity.class);
                academicInformationEntity.setStudentEntity(studentEntity);

                academicInformationEntities.add(academicInformationEntity);
            }
        }
        studentEntity.setAcademicInformationEntity(academicInformationEntities);

        // save student entity
        StudentEntity savedEntity = studentRepository.save(studentEntity);


        // return saved student DTO
        StudentDTO savedDTO = modelMapper.map(savedEntity, StudentDTO.class);

        // Set Parent DTOs
        List<ParentDTO> parentDTOs = new ArrayList<>();
        if (savedEntity.getParentEntities() != null && !savedEntity.getParentEntities().isEmpty()) {
            for (ParentEntity parentEntity : savedEntity.getParentEntities()) {
                ParentDTO parentDTO = modelMapper.map(parentEntity, ParentDTO.class);
                parentDTOs.add(parentDTO);
            }
        }
        savedDTO.setParentEntities(parentDTOs);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (savedEntity.getStudentDocumentEntities() != null && !savedEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : savedEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        savedDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (savedEntity.getAcademicInformationEntity() != null && !savedEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : savedEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =
                        modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        savedDTO.setAcademicInformation(academicInformationDTOs);

        log.info("Exit from saveStudent");

        return savedDTO;
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        log.info("Enter into updateStudent");

        if (studentDTO == null) {
            throw new CustomException("Student info cannot be null",
                    HttpStatus.PRECONDITION_FAILED);
        }

        StudentEntity existingStudentEntity = studentRepository.findById(studentDTO.getStudentId())
                .orElseThrow(() ->
                        new CustomException("Student not found", HttpStatus.NOT_FOUND));

        // Update basic fields
        existingStudentEntity.setFirstName(studentDTO.getFirstName());
        existingStudentEntity.setLastName(studentDTO.getLastName());
        existingStudentEntity.setGender(studentDTO.getGender());
        existingStudentEntity.setDOB(studentDTO.getDob());
        existingStudentEntity.setAddress(studentDTO.getAddress());
        existingStudentEntity.setBloodGroup(studentDTO.getBloodGroup());
        existingStudentEntity.setCategory(studentDTO.getCategory());
        existingStudentEntity.setReligion(studentDTO.getReligion());
        existingStudentEntity.setCaste(studentDTO.getCaste());
        existingStudentEntity.setNationality(studentDTO.getNationality());
        existingStudentEntity.setStatus(studentDTO.getStatus());

        // delete removed parents from existing entity
        Set<Long> requestParentIds = studentDTO.getParentEntities().stream()
                .map(ParentDTO::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingStudentEntity.getParentEntities().removeIf(parent ->
                parent.getParentId() != null &&
                        !requestParentIds.contains(parent.getParentId()));

        // Existing parents from DB
        Map<Long, ParentEntity> existingParents = existingStudentEntity.getParentEntities()
                .stream()
                .collect(Collectors.toMap(ParentEntity::getParentId, Function.identity()));

        if (studentDTO.getParentEntities() != null) {

            for (ParentDTO parentDTO : studentDTO.getParentEntities()) {

                ParentEntity parentEntity;

                if (parentDTO.getParentId() != null &&
                        existingParents.containsKey(parentDTO.getParentId())) {

                    // Update existing
                    parentEntity = existingParents.get(parentDTO.getParentId());

                } else {

                    // New parent
                    parentEntity = new ParentEntity();
                    parentEntity.setStudentEntity(existingStudentEntity);

                    existingStudentEntity.getParentEntities().add(parentEntity);

                }

                parentEntity.setName(parentDTO.getName());
                parentEntity.setRelation(parentDTO.getRelation());
                parentEntity.setOccupation(parentDTO.getOccupation());
                parentEntity.setPhone(parentDTO.getPhone());
                parentEntity.setEmail(parentDTO.getEmail());
                parentEntity.setAddress(parentDTO.getAddress());
                parentEntity.setAnnualIncome(parentDTO.getAnnualIncome());

            }
        }

        // Delete removed student documents
        Set<Long> requestDocumentIds = studentDTO.getStudentDocuments().stream()
                .map(StudentDocumentDTO::getStudentDocumentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingStudentEntity.getStudentDocumentEntities().removeIf(document ->
                document.getStudentDocumentId() != null &&
                        !requestDocumentIds.contains(document.getStudentDocumentId()));

        // Existing documents map
        Map<Long, StudentDocumentEntity> existingDocuments =
                existingStudentEntity.getStudentDocumentEntities().stream()
                        .collect(Collectors.toMap(
                                StudentDocumentEntity::getStudentDocumentId,
                                Function.identity()));

        for (StudentDocumentDTO documentDTO : studentDTO.getStudentDocuments()) {

            StudentDocumentEntity documentEntity;

            if (documentDTO.getStudentDocumentId() != null &&
                    existingDocuments.containsKey(documentDTO.getStudentDocumentId())) {

                // Update existing
                documentEntity = existingDocuments.get(documentDTO.getStudentDocumentId());

            } else {

                // New document
                documentEntity = new StudentDocumentEntity();
                documentEntity.setStudentEntity(existingStudentEntity);
                if (documentDTO.getDocument() != null) {
                    documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                }

                existingStudentEntity.getStudentDocumentEntities().add(documentEntity);
            }

            documentEntity.setDocumentName(documentDTO.getDocumentName());
            documentEntity.setUploadDate(documentDTO.getUploadDate());
        }

        // Delete removed academic information
        Set<Long> requestAcademicIds = studentDTO.getAcademicInformation().stream()
                .map(AcademicInformationDTO::getAcademicInformationId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingStudentEntity.getAcademicInformationEntity().removeIf(academic ->
                academic.getAcademicInformationId() != null &&
                        !requestAcademicIds.contains(academic.getAcademicInformationId()));

        // Existing academic map
        Map<Long, AcademicInformationEntity> existingAcademics =
                existingStudentEntity.getAcademicInformationEntity().stream()
                        .collect(Collectors.toMap(
                                AcademicInformationEntity::getAcademicInformationId,
                                Function.identity()));

        for (AcademicInformationDTO academicDTO : studentDTO.getAcademicInformation()) {

            AcademicInformationEntity academicEntity;

            if (academicDTO.getAcademicInformationId() != null &&
                    existingAcademics.containsKey(academicDTO.getAcademicInformationId())) {

                // Update existing
                academicEntity = existingAcademics.get(academicDTO.getAcademicInformationId());

            } else {

                // New academic record
                academicEntity = new AcademicInformationEntity();
                academicEntity.setStudentEntity(existingStudentEntity);

                existingStudentEntity.getAcademicInformationEntity().add(academicEntity);
            }

            academicEntity.setAdmissionNo(academicDTO.getAdmissionNo());
            academicEntity.setAdmissionDate(academicDTO.getAdmissionDate());
            academicEntity.setStandard(academicDTO.getStandard());
            academicEntity.setSection(academicDTO.getSection());
            academicEntity.setRollNo(academicDTO.getRollNo());
            academicEntity.setAcademicYear(academicDTO.getAcademicYear());
        }

        // update student entity
        StudentEntity savedEntity = studentRepository.save(existingStudentEntity);

        // return updated student DTO
        StudentDTO savedDTO = modelMapper.map(savedEntity, StudentDTO.class);

        // Set Parent DTOs
        List<ParentDTO> parentDTOs = new ArrayList<>();
        if (savedEntity.getParentEntities() != null && !savedEntity.getParentEntities().isEmpty()) {
            for (ParentEntity parentEntity : savedEntity.getParentEntities()) {
                ParentDTO parentDTO = modelMapper.map(parentEntity, ParentDTO.class);
                parentDTOs.add(parentDTO);
            }
        }
        savedDTO.setParentEntities(parentDTOs);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (savedEntity.getStudentDocumentEntities() != null && !savedEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : savedEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        savedDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (savedEntity.getAcademicInformationEntity() != null && !savedEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : savedEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =
                        modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        savedDTO.setAcademicInformation(academicInformationDTOs);

        log.info("Exist from updateStudent");
        return savedDTO;
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        log.info("Enter into getStudentById");

        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentDTO savedDTO = modelMapper.map(studentEntity, StudentDTO.class);

        // Set Parent DTOs
        List<ParentDTO> parentDTOs = new ArrayList<>();
        if (studentEntity.getParentEntities() != null && !studentEntity.getParentEntities().isEmpty()) {
            for (ParentEntity parentEntity : studentEntity.getParentEntities()) {
                ParentDTO parentDTO = modelMapper.map(parentEntity, ParentDTO.class);
                parentDTOs.add(parentDTO);
            }
        }
        savedDTO.setParentEntities(parentDTOs);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (studentEntity.getStudentDocumentEntities() != null && !studentEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : studentEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        savedDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (studentEntity.getAcademicInformationEntity() != null && !studentEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : studentEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =
                        modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        savedDTO.setAcademicInformation(academicInformationDTOs);

        log.info("Exit from getStudentById");
        return savedDTO;
    }

    @Override
    public String deleteStudent(Long studentId) {
        log.info("Enter into deleteStudent");

        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        studentRepository.delete(studentEntity);

        log.info("Exit from deleteStudent");
        return "Student record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllStudentsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllStudentsByFilter");

        Page<StudentEntity> studentEntityPage;
        List<StudentEntity> studentEntities;
        long totalElement;

        CustomQuerySpecification<StudentEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if(paginate){
            studentEntityPage = studentRepository.findAll(customQuerySpecification, pageable);
            studentEntities = studentEntityPage.getContent();
            totalElement = studentEntityPage.getTotalElements();
        }else{
            studentEntities = studentRepository.findAll(customQuerySpecification);
            totalElement = studentEntities.size();
        }

        List<StudentDTO> studentDTOS = studentEntities.stream()
                .map(s -> {
                    StudentDTO dto = modelMapper.map(s, StudentDTO.class);

                    // Set Parent DTOs
                    List<ParentDTO> parentDTOs = new ArrayList<>();
                    if (s.getParentEntities() != null && !s.getParentEntities().isEmpty()) {
                        for (ParentEntity parentEntity : s.getParentEntities()) {
                            ParentDTO parentDTO = modelMapper.map(parentEntity, ParentDTO.class);
                            parentDTOs.add(parentDTO);
                        }
                    }
                    dto.setParentEntities(parentDTOs);

                    // Set Student Document DTOs
                    List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
                    if (s.getStudentDocumentEntities() != null && !s.getStudentDocumentEntities().isEmpty()) {
                        for (StudentDocumentEntity studentDocumentEntity : s.getStudentDocumentEntities()) {
                            StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                            studentDocumentDTOs.add(studentDocumentDTO);
                        }
                    }
                    dto.setStudentDocuments(studentDocumentDTOs);

                    // Set Academic Information DTOs
                    List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
                    if (s.getAcademicInformationEntity() != null && !s.getAcademicInformationEntity().isEmpty()) {
                        for (AcademicInformationEntity academicInformationEntity : s.getAcademicInformationEntity()) {
                            AcademicInformationDTO academicInformationDTO =
                                    modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                            academicInformationDTOs.add(academicInformationDTO);
                        }
                    }
                    dto.setAcademicInformation(academicInformationDTOs);

                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Exit from getAllStudentsByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", studentDTOS);
        result.put("Total", totalElement);
        return result;
    }

}
