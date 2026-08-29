package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ScreenMasterDTO;
import com.flint.sample_be_springboot.dto.SignUpDTO;
import com.flint.sample_be_springboot.dto.UserDTO;
import com.flint.sample_be_springboot.dto.student.*;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.UserScreenAccessEntity;
import com.flint.sample_be_springboot.entity.student.*;
import com.flint.sample_be_springboot.enums.Role;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.repository.student.AcademicInformationRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import com.flint.sample_be_springboot.util.GenerateCodes;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl extends BaseService implements StudentService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicInformationRepository academicInformationRepository;


    @Override
    public Map<String, Object> saveStudent(StudentDTO studentDTO) {
        log.info("Enter into saveStudent");

        if (studentDTO == null) {
            throw new CustomException("Student info cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

//        StudentEntity existingStudentEntity = studentRepository.findByAadhaarCard(studentDTO.getAadhaarCard());
//        if (!ObjectUtils.isEmpty(existingStudentEntity)) {
//            throw new CustomException("Student aadhaar no. already registered", HttpStatus.PRECONDITION_FAILED);
//

        StudentEntity studentEntity = modelMapper.map(studentDTO, StudentEntity.class);

        String lastStudentCode = studentRepository.findLastStudentCode() != null ? studentRepository.findLastStudentCode() : null;
        String nextStudentCode = GenerateCodes.generateStudentCode(lastStudentCode);
        System.err.println(nextStudentCode);
        studentEntity.setStudentCode(nextStudentCode);


        studentEntity.setPhone(studentDTO.getParentDTO().getPhone());

        if (studentDTO.getProfileImg() != null) {
            studentEntity.setProfileImg(Base64.getDecoder().decode(studentDTO.getProfileImg()));
        }

        studentEntity.setAuditDetails(addAuditDetails(studentEntity.getAuditDetails()));

        // set parent entities
//        List<ParentEntity> parentEntities = new ArrayList<>();
//        if (studentDTO.getParentEntities() != null && !studentDTO.getParentEntities().isEmpty()) {
//            for (ParentDTO parentDTO : studentDTO.getParentEntities()) {
//                ParentEntity parentEntity = modelMapper.map(parentDTO, ParentEntity.class);
//                parentEntity.setStudentEntity(studentEntity);
//                parentEntities.add(parentEntity);
//            }
//        }
//        studentEntity.setParentEntities(parentEntities);

        if (studentDTO.getParentDTO() != null) {

            ParentEntity parentEntity = modelMapper.map(studentDTO.getParentDTO(), ParentEntity.class);

            // Set both sides of relationship
            parentEntity.setStudentEntity(studentEntity);
            studentEntity.setParentEntity(parentEntity);

            // Parent audit details
            parentEntity.setAuditDetails(addAuditDetails(parentEntity.getAuditDetails()));
        }

        // set student documents
        List<StudentDocumentEntity> studentDocumentEntities = new ArrayList<>();
        if (studentDTO.getStudentDocuments() != null && !studentDTO.getStudentDocuments().isEmpty()) {
            for (StudentDocumentDTO studentDocumentDTO : studentDTO.getStudentDocuments()) {
                StudentDocumentEntity studentDocumentEntity = modelMapper.map(studentDocumentDTO, StudentDocumentEntity.class);
                if (studentDocumentDTO.getDocument() != null) {
                    studentDocumentEntity.setDocument(Base64.getDecoder().decode(studentDocumentDTO.getDocument()));
                }
                studentDocumentEntity.setStudentEntity(studentEntity);
                studentDocumentEntities.add(studentDocumentEntity);
            }
        }
        studentEntity.setStudentDocumentEntities(studentDocumentEntities);

        // set student academic information
        String lastAdmissionCode = studentRepository.findLastAdmissionNo() != null ? studentRepository.findLastAdmissionNo() : null;
        String nextStudentAdmissionCode = GenerateCodes.generateStudentAdmissionNo(lastAdmissionCode);

        studentEntity.setAdmissionNo(nextStudentAdmissionCode);

        Set<String> academicYears = new HashSet<>();

        List<AcademicInformationEntity> academicInformationEntities = new ArrayList<>();

        if (studentDTO.getAcademicInformation() != null
                && !studentDTO.getAcademicInformation().isEmpty()) {

            for (AcademicInformationDTO academicInformationDTO :
                    studentDTO.getAcademicInformation()) {

                String academicYear = academicInformationDTO.getAcademicYear();

                if (academicYear == null || academicYear.trim().isEmpty()) {
                    throw new CustomException(
                            "Academic year can't be null or empty",
                            HttpStatus.BAD_REQUEST
                    );
                }

                // Check duplicate academic year in request
                String normalizedAcademicYear = academicYear.trim().toLowerCase();

                if (!academicYears.add(normalizedAcademicYear)) {
                    throw new CustomException(
                            "Academic information already exists for academic year "
                                    + academicYear,
                            HttpStatus.BAD_REQUEST
                    );
                }

                AcademicInformationEntity academicInformationEntity =
                        modelMapper.map(
                                academicInformationDTO,
                                AcademicInformationEntity.class
                        );

                academicInformationEntity.setStudentEntity(studentEntity);
                academicInformationEntity.setAdmissionNo(nextStudentAdmissionCode);

                academicInformationEntities.add(academicInformationEntity);
            }
        }

        studentEntity.setAcademicInformationEntity(academicInformationEntities);

        // save student entity
        StudentEntity savedEntity = studentRepository.save(studentEntity);

        SignUpDTO signUpDTO = modelMapper.map(studentDTO.getParentDTO(), SignUpDTO.class);
        signUpDTO.setStudentId(savedEntity.getStudentId());
        signUpDTO.setUserName(studentDTO.getParentDTO().getPhone());
        signUpDTO.setMobileNo(studentDTO.getParentDTO().getPhone());
        signUpDTO.setFirstName(studentDTO.getFirstName());
        signUpDTO.setLastName(studentDTO.getLastName());
        signUpDTO.setRole(Role.STUDENT);
        signUpDTO.setEmail(studentDTO.getParentDTO().getEmail());
        signUpDTO.setAadhaarNo(studentDTO.getAadhaarCard());
        signUpDTO.setDOB(studentDTO.getDOB());


        UserDTO userDTO = userService.saveUser(signUpDTO);

        // return saved student DTO
        StudentDTO savedDTO = modelMapper.map(savedEntity, StudentDTO.class);

        // Set Parent DTOs
        ParentDTO parentDTO = null;

        if (savedEntity.getParentEntity() != null) {

            parentDTO = modelMapper.map(savedEntity.getParentEntity(), ParentDTO.class);
            parentDTO.setStudentId(savedEntity.getStudentId());
        }

        savedDTO.setParentDTO(parentDTO);

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

        // set student results
        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();
        if(studentEntity.getStudentResultEntities() != null && !studentEntity.getStudentResultEntities().isEmpty()){
            for(StudentResultEntity studentResultEntity : studentEntity.getStudentResultEntities()){
                StudentResultDTO studentResultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);

                List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
                if(studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()){
                    for(ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()){
                        ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                        examSubjectsDTOS.add(examSubjectsDTO);
                    }
                }
                studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);
                studentResultDTOS.add(studentResultDTO);
            }
        }
        savedDTO.setStudentResultDTOS(studentResultDTOS);

        List<StudentAchievementsDTO> studentAchievementsDTOS = new ArrayList<>();
        if(studentEntity.getStudentAchievementsEntities() != null && !studentEntity.getStudentAchievementsEntities().isEmpty()){
            for(StudentAchievementsEntity studentAchievementsEntity : studentEntity.getStudentAchievementsEntities()){
                StudentAchievementsDTO achievementsDTO = modelMapper.map(studentAchievementsEntity, StudentAchievementsDTO.class);
                studentAchievementsDTOS.add(achievementsDTO);
            }
        }
        savedDTO.setStudentAchievementsDTOS(studentAchievementsDTOS);

        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
        if(savedEntity.getStudentFeeEntities() != null && !savedEntity.getStudentFeeEntities().isEmpty()){
            for(StudentFeeEntity studentFeeEntity : savedEntity.getStudentFeeEntities()){
                StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);

                List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
                if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
                    for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
                        FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                        feePaymentDTOS.add(feePaymentDTO);
                    }
                }
                studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
                studentFeeDTOS.add(studentFeeDTO);
            }
        }
        savedDTO.setStudentFeeDTOS(studentFeeDTOS);

        log.info("Exit from saveStudent");

        Map<String, Object> map = new HashMap<>();
        map.put("Student information", savedDTO);
        map.put("User DTO", userDTO);
        return map;
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
        existingStudentEntity.setStudentCode(studentDTO.getStudentCode());
        existingStudentEntity.setDOB(studentDTO.getDOB());
        existingStudentEntity.setAddress(studentDTO.getAddress());
        existingStudentEntity.setBloodGroup(studentDTO.getBloodGroup());
        existingStudentEntity.setCategory(studentDTO.getCategory());
        existingStudentEntity.setReligion(studentDTO.getReligion());
        existingStudentEntity.setCaste(studentDTO.getCaste());
        existingStudentEntity.setNationality(studentDTO.getNationality());
        existingStudentEntity.setStatus(studentDTO.getStatus());
        existingStudentEntity.setAdmissionNo(existingStudentEntity.getAdmissionNo());
        existingStudentEntity.setPhone(studentDTO.getParentDTO().getPhone());
        existingStudentEntity.setAadhaarCard(studentDTO.getAadhaarCard());
        existingStudentEntity.setAuditDetails(addAuditDetails(existingStudentEntity.getAuditDetails()));

        if (studentDTO.getProfileImg() != null) {
            existingStudentEntity.setProfileImg(Base64.getDecoder().decode(studentDTO.getProfileImg()));
        } else {
            existingStudentEntity.setProfileImg(null);
        }

        // Setting parentInfo
        if (studentDTO.getParentDTO() != null) {

            ParentEntity parentEntity = modelMapper.map(studentDTO.getParentDTO(), ParentEntity.class);

            // Set both sides of relationship
            parentEntity.setStudentEntity(existingStudentEntity);
            existingStudentEntity.setParentEntity(parentEntity);

            // Parent audit
            parentEntity.setAuditDetails(
                    addAuditDetails(parentEntity.getAuditDetails())
            );

            // If phone is stored in StudentEntity also
            if (studentDTO.getParentDTO().getPhone() != null) {
                existingStudentEntity.setPhone(studentDTO.getParentDTO().getPhone());
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
        Map<Long, StudentDocumentEntity> existingDocuments = existingStudentEntity.getStudentDocumentEntities().stream()
                .collect(Collectors.toMap(StudentDocumentEntity::getStudentDocumentId, Function.identity()));

        for (StudentDocumentDTO documentDTO : studentDTO.getStudentDocuments()) {

            StudentDocumentEntity documentEntity;

            if (documentDTO.getStudentDocumentId() != null &&
                    existingDocuments.containsKey(documentDTO.getStudentDocumentId())) {

                // Update existing
                documentEntity = existingDocuments.get(documentDTO.getStudentDocumentId());

                if (documentDTO.getDocument() != null) {
                    documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                }

                documentEntity.setAuditDetails(addAuditDetails(documentEntity.getAuditDetails()));

            } else {

                // New document
                documentEntity = new StudentDocumentEntity();
                documentEntity.setStudentEntity(existingStudentEntity);
                if (documentDTO.getDocument() != null) {
                    documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                }

                documentEntity.setAuditDetails(addAuditDetails(documentEntity.getAuditDetails()));
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
                        .collect(Collectors.toMap(AcademicInformationEntity::getAcademicInformationId, Function.identity()));

        // Track academic years coming in the request
        Set<String> academicYears = new HashSet<>();

        for (AcademicInformationDTO academicDTO : studentDTO.getAcademicInformation()) {

            String academicYear = academicDTO.getAcademicYear();

            if (academicYear == null || academicYear.trim().isEmpty()) {
                throw new CustomException(
                        "Academic year can't be null or empty",
                        HttpStatus.BAD_REQUEST
                );
            }

            String normalizedAcademicYear = academicYear.trim().toLowerCase();

            // Check duplicate academic year in request itself
            if (!academicYears.add(normalizedAcademicYear)) {
                throw new CustomException(
                        "Academic information already exists for academic year "
                                + academicYear,
                        HttpStatus.BAD_REQUEST
                );
            }

            // Check duplicate academic year against existing DB records
            boolean alreadyExists = existingStudentEntity
                    .getAcademicInformationEntity()
                    .stream()
                    .anyMatch(existing ->
                            existing.getAcademicYear() != null
                                    && existing.getAcademicYear()
                                    .trim()
                                    .equalsIgnoreCase(academicYear.trim())
                                    && !Objects.equals(
                                    existing.getAcademicInformationId(),
                                    academicDTO.getAcademicInformationId()
                            )
                    );

            if (alreadyExists) {
                throw new CustomException(
                        "Academic information already exists for academic year "
                                + academicYear,
                        HttpStatus.BAD_REQUEST
                );
            }

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

            academicEntity.setAdmissionNo(existingStudentEntity.getAdmissionNo());
            academicEntity.setAdmissionDate(academicDTO.getAdmissionDate());
            academicEntity.setStandard(academicDTO.getStandard());
            academicEntity.setDivision(academicDTO.getDivision());
            academicEntity.setMedium(academicDTO.getMedium());
            academicEntity.setRollNo(academicDTO.getRollNo());
            academicEntity.setAcademicYear(academicDTO.getAcademicYear());
        }

        // update student entity
        StudentEntity savedEntity = studentRepository.save(existingStudentEntity);

        UserEntity user = userRepository.findByStudentEntity_StudentId(savedEntity.getStudentId())
                .orElseThrow(() -> new CustomException("Student user not found", HttpStatus.NOT_FOUND));

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(savedEntity.getUserEntity().getUserId());
        userDTO.setUserName(studentDTO.getParentDTO().getPhone());
        userDTO.setMobileNo(studentDTO.getParentDTO().getPhone());
        userDTO.setFirstName(studentDTO.getFirstName());
        userDTO.setLastName(studentDTO.getLastName());
        userDTO.setRole(Role.STUDENT);
        userDTO.setEmail(studentDTO.getParentDTO().getEmail());

        List<ScreenMasterDTO> screens = user.getScreenAccesses()
                .stream()
                .map(UserScreenAccessEntity::getScreen)
                .map(screen -> modelMapper.map(screen, ScreenMasterDTO.class))
                .toList();

        userDTO.setScreens(screens);

        UserDTO updatedUserDTO = userService.updateUser(userDTO);

        // return updated student DTO
        StudentDTO updatedStudentDTO = modelMapper.map(savedEntity, StudentDTO.class);

        // Set Parent DTO
        if (savedEntity.getParentEntity() != null) {

            ParentDTO parentDTO = modelMapper.map(savedEntity.getParentEntity(), ParentDTO.class);
            updatedStudentDTO.setParentDTO(parentDTO);

        } else {

            updatedStudentDTO.setParentDTO(null);
        }


        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (savedEntity.getStudentDocumentEntities() != null && !savedEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : savedEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        studentDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (savedEntity.getAcademicInformationEntity() != null && !savedEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : savedEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);
                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        updatedStudentDTO.setAcademicInformation(academicInformationDTOs);

        // set student results
        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();
        if(savedEntity.getStudentResultEntities() != null && !savedEntity.getStudentResultEntities().isEmpty()){
            for(StudentResultEntity studentResultEntity : savedEntity.getStudentResultEntities()){
                StudentResultDTO studentResultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);

                List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
                if(studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()){
                    for(ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()){
                        ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                        examSubjectsDTOS.add(examSubjectsDTO);
                    }
                }
                studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);
                studentResultDTOS.add(studentResultDTO);
            }
        }
        updatedStudentDTO.setStudentResultDTOS(studentResultDTOS);

        // set student achievements
        List<StudentAchievementsDTO> studentAchievementsDTOS = new ArrayList<>();
        if(savedEntity.getStudentAchievementsEntities() != null && !savedEntity.getStudentAchievementsEntities().isEmpty()){
            for(StudentAchievementsEntity studentAchievementsEntity : savedEntity.getStudentAchievementsEntities()){
                StudentAchievementsDTO achievementsDTO = modelMapper.map(studentAchievementsEntity, StudentAchievementsDTO.class);
                studentAchievementsDTOS.add(achievementsDTO);
            }
        }
        updatedStudentDTO.setStudentAchievementsDTOS(studentAchievementsDTOS);

        // set student fees
        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
        if(savedEntity.getStudentFeeEntities() != null && !savedEntity.getStudentFeeEntities().isEmpty()){
            for(StudentFeeEntity studentFeeEntity : savedEntity.getStudentFeeEntities()){
                StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);

                List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
                if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
                    for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
                        FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                        feePaymentDTOS.add(feePaymentDTO);
                    }
                }
                studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
                studentFeeDTOS.add(studentFeeDTO);
            }
        }
        updatedStudentDTO.setStudentFeeDTOS(studentFeeDTOS);

        log.info("Exist from updateStudent");
        return updatedStudentDTO;
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        log.info("Enter into getStudentById");

        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentDTO studentDTO = modelMapper.map(studentEntity, StudentDTO.class);

        LocalDate startDate = getStartDate();
        LocalDate endDate = getEndDate();
        String currentAcademicYear = startDate.getYear() + "-" + endDate.getYear();

        if (studentEntity.getProfileImg() != null) {
            String img = Base64.getEncoder().encodeToString(studentEntity.getProfileImg());

            studentDTO.setProfileImg(img);
        }

        // Set Parent DTO
        ParentDTO parentDTO = null;
        if (studentEntity.getParentEntity() != null) {
            parentDTO = modelMapper.map(studentEntity.getParentEntity(), ParentDTO.class);
        }
        studentDTO.setParentDTO(parentDTO);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (studentEntity.getStudentDocumentEntities() != null && !studentEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : studentEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        studentDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (studentEntity.getAcademicInformationEntity() != null && !studentEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : studentEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =
                        modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        studentDTO.setAcademicInformation(academicInformationDTOs);

        // set student results
//        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();
//        if(studentEntity.getStudentResultEntities() != null && !studentEntity.getStudentResultEntities().isEmpty()){
//            for(StudentResultEntity studentResultEntity : studentEntity.getStudentResultEntities()){
//                StudentResultDTO studentResultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);
//
//                List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
//                if(studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()){
//                    for(ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()){
//                        ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
//                        examSubjectsDTOS.add(examSubjectsDTO);
//                    }
//                }
//                studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);
//                studentResultDTOS.add(studentResultDTO);
//            }
//        }
        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();

        if (studentEntity.getStudentResultEntities() != null
                && !studentEntity.getStudentResultEntities().isEmpty()) {

            studentResultDTOS = studentEntity.getStudentResultEntities()
                    .stream()

                    // Filter results based on current academic year
                    .filter(result ->
                            currentAcademicYear.equals(result.getAcademicYear()))

                    // Map StudentResultEntity -> StudentResultDTO
                    .map(result -> {

                        StudentResultDTO studentResultDTO =
                                modelMapper.map(result, StudentResultDTO.class);

                        // Set exam subjects
                        List<ExamSubjectsDTO> examSubjectsDTOS =
                                result.getExamSubjectsEntities() == null
                                        ? new ArrayList<>()
                                        : result.getExamSubjectsEntities()
                                        .stream()
                                        .map(examSubjectsEntity ->
                                                modelMapper.map(
                                                        examSubjectsEntity,
                                                        ExamSubjectsDTO.class))
                                        .toList();

                        studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

                        return studentResultDTO;
                    })
                    .toList();
        }
        studentDTO.setStudentResultDTOS(studentResultDTOS);

        // set student achievements
        List<StudentAchievementsDTO> studentAchievementsDTOS = new ArrayList<>();
        if(studentEntity.getStudentAchievementsEntities() != null && !studentEntity.getStudentAchievementsEntities().isEmpty()){
            for(StudentAchievementsEntity studentAchievementsEntity : studentEntity.getStudentAchievementsEntities()){
                StudentAchievementsDTO achievementsDTO = modelMapper.map(studentAchievementsEntity, StudentAchievementsDTO.class);
                studentAchievementsDTOS.add(achievementsDTO);
            }
        }
        studentDTO.setStudentAchievementsDTOS(studentAchievementsDTOS);

//        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
//        if(studentEntity.getStudentFeeEntities() != null && !studentEntity.getStudentFeeEntities().isEmpty()){
//            for(StudentFeeEntity studentFeeEntity : studentEntity.getStudentFeeEntities()){
//                StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);
//
//                List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
//                if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
//                    for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
//                        FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
//                        feePaymentDTOS.add(feePaymentDTO);
//                    }
//                }
//                studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
//                studentFeeDTOS.add(studentFeeDTO);
//            }
//        }
//        studentDTO.setStudentFeeDTOS(studentFeeDTOS);
        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();

        if (studentEntity.getStudentFeeEntities() != null
                && !studentEntity.getStudentFeeEntities().isEmpty()) {

            studentFeeDTOS = studentEntity.getStudentFeeEntities()
                    .stream()

                    // Filter fees based on current academic year
                    .filter(fee ->
                            currentAcademicYear.equals(fee.getAcademicYear()))

                    // Map StudentFeeEntity -> StudentFeeDTO
                    .map(fee -> {

                        StudentFeeDTO studentFeeDTO =
                                modelMapper.map(fee, StudentFeeDTO.class);

                        // Set fee payments
                        List<FeePaymentDTO> feePaymentDTOS =
                                fee.getFeePaymentEntities() == null
                                        ? new ArrayList<>()
                                        : fee.getFeePaymentEntities()
                                        .stream()
                                        .map(feePaymentEntity ->
                                                modelMapper.map(
                                                        feePaymentEntity,
                                                        FeePaymentDTO.class))
                                        .toList();

                        studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);

                        return studentFeeDTO;
                    })
                    .toList();
        }

        studentDTO.setStudentFeeDTOS(studentFeeDTOS);

        log.info("Exit from getStudentById");
        return studentDTO;
    }

    @Override
    @Transactional
    public String deleteStudent(Long studentId) {

        log.info("Enter into deleteStudent");

        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new CustomException("Student not found", HttpStatus.NOT_FOUND));

        // Get associated user
        UserEntity userEntity = studentEntity.getUserEntity();

        if (userEntity != null) {
            // Break the FK relationship first
            userEntity.setStudentEntity(null);
            studentEntity.setUserEntity(null);

            // Delete User
            userRepository.delete(userEntity);
        }

        // Now delete student
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

        if (paginate) {
            studentEntityPage = studentRepository.findAll(customQuerySpecification, pageable);
            studentEntities = studentEntityPage.getContent();
            totalElement = studentEntityPage.getTotalElements();
        } else {
            studentEntities = studentRepository.findAll(customQuerySpecification);
            totalElement = studentEntities.size();
        }


        List<StudentDTO> studentDTOS = studentEntities.stream()
                .map(s -> {
                    StudentDTO dto = modelMapper.map(s, StudentDTO.class);

                    if (s.getProfileImg() != null) {
                        String img = Base64.getEncoder().encodeToString(s.getProfileImg());

                        dto.setProfileImg(img);
                    }

                    // Set Parent DTOs
                    ParentDTO parentDTO = null;
                    if (s.getParentEntity() != null) {
                        parentDTO = modelMapper.map(s.getParentEntity(), ParentDTO.class);
                    }
                    dto.setParentDTO(parentDTO);

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

                    // set student results
                    List<StudentResultDTO> studentResultDTOS = new ArrayList<>();
                    if(s.getStudentResultEntities() != null && !s.getStudentResultEntities().isEmpty()){
                        for(StudentResultEntity studentResultEntity : s.getStudentResultEntities()){
                            StudentResultDTO studentResultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);

                            List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
                            if(studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()){
                                for(ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()){
                                    ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                                    examSubjectsDTOS.add(examSubjectsDTO);
                                }
                            }
                            studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);
                            studentResultDTOS.add(studentResultDTO);
                        }
                    }
                    dto.setStudentResultDTOS(studentResultDTOS);

                    // set student achievements
                    List<StudentAchievementsDTO> studentAchievementsDTOS = new ArrayList<>();
                    if(s.getStudentAchievementsEntities() != null && !s.getStudentAchievementsEntities().isEmpty()){
                        for(StudentAchievementsEntity studentAchievementsEntity : s.getStudentAchievementsEntities()){
                            StudentAchievementsDTO achievementsDTO = modelMapper.map(studentAchievementsEntity, StudentAchievementsDTO.class);
                            studentAchievementsDTOS.add(achievementsDTO);
                        }
                    }
                    dto.setStudentAchievementsDTOS(studentAchievementsDTOS);

                    // set student fees
                    List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
                    if(s.getStudentFeeEntities() != null && !s.getStudentFeeEntities().isEmpty()){
                        for(StudentFeeEntity studentFeeEntity : s.getStudentFeeEntities()){
                            StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);

                            List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
                            if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
                                for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
                                    FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                                    feePaymentDTOS.add(feePaymentDTO);
                                }
                            }
                            studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
                            studentFeeDTOS.add(studentFeeDTO);
                        }
                    }
                    dto.setStudentFeeDTOS(studentFeeDTOS);

                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Exit from getAllStudentsByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("Data", studentDTOS);
        result.put("Total", totalElement);
        return result;
    }

    @Override
    public StudentDTO getStudentByUserId(Long userId) {
        log.info("Enter into getStudentById");

        StudentEntity studentEntity = studentRepository.findByUserEntity_UserId(userId)
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentDTO studentDTO = modelMapper.map(studentEntity, StudentDTO.class);

        if (studentEntity.getProfileImg() != null) {
            String img = Base64.getEncoder().encodeToString(studentEntity.getProfileImg());

            studentDTO.setProfileImg(img);
        }

        // Set Parent DTO
        ParentDTO parentDTO = null;
        if (studentEntity.getParentEntity() != null) {
            parentDTO = modelMapper.map(studentEntity.getParentEntity(), ParentDTO.class);
        }
        studentDTO.setParentDTO(parentDTO);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (studentEntity.getStudentDocumentEntities() != null && !studentEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : studentEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        studentDTO.setStudentDocuments(studentDocumentDTOs);

        // Set Academic Information DTOs
        List<AcademicInformationDTO> academicInformationDTOs = new ArrayList<>();
        if (studentEntity.getAcademicInformationEntity() != null && !studentEntity.getAcademicInformationEntity().isEmpty()) {
            for (AcademicInformationEntity academicInformationEntity : studentEntity.getAcademicInformationEntity()) {
                AcademicInformationDTO academicInformationDTO =
                        modelMapper.map(academicInformationEntity, AcademicInformationDTO.class);

                academicInformationDTOs.add(academicInformationDTO);
            }
        }
        studentDTO.setAcademicInformation(academicInformationDTOs);

        // set student results
        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();
        if(studentEntity.getStudentResultEntities() != null && !studentEntity.getStudentResultEntities().isEmpty()){
            for(StudentResultEntity studentResultEntity : studentEntity.getStudentResultEntities()){
                StudentResultDTO studentResultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);

                List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
                if(studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()){
                    for(ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()){
                        ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                        examSubjectsDTOS.add(examSubjectsDTO);
                    }
                }
                studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);
                studentResultDTOS.add(studentResultDTO);
            }
        }
        studentDTO.setStudentResultDTOS(studentResultDTOS);

        // set student achievements
        List<StudentAchievementsDTO> studentAchievementsDTOS = new ArrayList<>();
        if(studentEntity.getStudentAchievementsEntities() != null && !studentEntity.getStudentAchievementsEntities().isEmpty()){
            for(StudentAchievementsEntity studentAchievementsEntity : studentEntity.getStudentAchievementsEntities()){
                StudentAchievementsDTO achievementsDTO = modelMapper.map(studentAchievementsEntity, StudentAchievementsDTO.class);
                studentAchievementsDTOS.add(achievementsDTO);
            }
        }
        studentDTO.setStudentAchievementsDTOS(studentAchievementsDTOS);

        // set student fee information
        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
        if(studentEntity.getStudentFeeEntities() != null && !studentEntity.getStudentFeeEntities().isEmpty()){
            for(StudentFeeEntity studentFeeEntity : studentEntity.getStudentFeeEntities()){
                StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);

                List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
                if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
                    for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
                        FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                        feePaymentDTOS.add(feePaymentDTO);
                    }
                }
                studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
                studentFeeDTOS.add(studentFeeDTO);
            }
        }
        studentDTO.setStudentFeeDTOS(studentFeeDTOS);

        log.info("Exit from getStudentById");
        return studentDTO;
    }


    @Override
    public Map<String, Object> getAllCurrentYearStudentsData(Map<String, Object> filter, Pageable pageable, boolean paginate) {

        log.info("Enter into getAllCurrentYearStudentsData");

        Map<String, Object> response = new HashMap<>();
        LocalDate startDate = getStartDate();
        LocalDate endDate = getEndDate();

        String currentAcademicYear = startDate.getYear() + "-" + endDate.getYear();

        String standard = filter.get("standard") != null
                ? filter.get("standard").toString()
                : null;

        String division = filter.get("division") != null
                ? filter.get("division").toString()
                : null;

        String medium = filter.get("medium") != null
                ? filter.get("medium").toString()
                : null;

        Page<StudentEntity> studentEntityPage = null;
        List<StudentEntity> studentEntities;
        long totalElement;

        if (paginate) {
            studentEntityPage = studentRepository.findAllCurrentYearStudents(currentAcademicYear, standard, division, medium, pageable);
            studentEntities = studentEntityPage.getContent();
            totalElement = studentEntityPage.getTotalElements();
        } else {
            studentEntities = studentRepository.findAllCurrentYearStudents(currentAcademicYear, standard, division, medium);
            totalElement = studentEntities.size();
        }

        // Convert Entity -> DTO
        List<StudentDTO> studentDTOs = studentEntities.stream()
                .map(student -> convertToCurrentYearStudentDTO(student, currentAcademicYear))
                .toList();

        response.put("data", studentDTOs);
        response.put("totalElements", totalElement);

        if (paginate && studentEntityPage != null) {

            response.put("totalPages", studentEntityPage.getTotalPages());
            response.put("currentPage", studentEntityPage.getNumber());
            response.put("pageSize", studentEntityPage.getSize());
        }

        log.info("Exit into getAllCurrentYearStudentsData");
        return response;

    }

    private StudentDTO convertToCurrentYearStudentDTO(StudentEntity studentEntity, String currentAcademicYear) {

        StudentDTO studentDTO = modelMapper.map(studentEntity, StudentDTO.class);

        List<AcademicInformationDTO> currentYearAcademicInfo = studentEntity.getAcademicInformationEntity()
                .stream()
                .filter(info ->
                        currentAcademicYear.equals(info.getAcademicYear()))
                .map(info ->
                        modelMapper.map(info, AcademicInformationDTO.class))
                .toList();

        studentDTO.setAcademicInformation(currentYearAcademicInfo);

        // Set Parent DTO
        ParentDTO parentDTO = null;
        if (studentEntity.getParentEntity() != null) {
            parentDTO = modelMapper.map(studentEntity.getParentEntity(), ParentDTO.class);
        }
        studentDTO.setParentDTO(parentDTO);

        // Set Student Document DTOs
        List<StudentDocumentDTO> studentDocumentDTOs = new ArrayList<>();
        if (studentEntity.getStudentDocumentEntities() != null && !studentEntity.getStudentDocumentEntities().isEmpty()) {
            for (StudentDocumentEntity studentDocumentEntity : studentEntity.getStudentDocumentEntities()) {
                StudentDocumentDTO studentDocumentDTO = modelMapper.map(studentDocumentEntity, StudentDocumentDTO.class);
                studentDocumentDTOs.add(studentDocumentDTO);
            }
        }
        studentDTO.setStudentDocuments(studentDocumentDTOs);

        // Set student result data for current academic year
        List<StudentResultDTO> studentResultDTOS = new ArrayList<>();

        if (studentEntity.getStudentResultEntities() != null
                && !studentEntity.getStudentResultEntities().isEmpty()) {

            studentResultDTOS = studentEntity.getStudentResultEntities()
                    .stream()

                    // Filter results based on current academic year
                    .filter(result ->
                            currentAcademicYear.equals(result.getAcademicYear()))

                    // Map StudentResultEntity -> StudentResultDTO
                    .map(result -> {

                        StudentResultDTO studentResultDTO =
                                modelMapper.map(result, StudentResultDTO.class);

                        // Set exam subjects
                        List<ExamSubjectsDTO> examSubjectsDTOS =
                                result.getExamSubjectsEntities() == null
                                        ? new ArrayList<>()
                                        : result.getExamSubjectsEntities()
                                        .stream()
                                        .map(examSubjectsEntity ->
                                                modelMapper.map(
                                                        examSubjectsEntity,
                                                        ExamSubjectsDTO.class))
                                        .toList();

                        studentResultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

                        return studentResultDTO;
                    })
                    .toList();
        }
        studentDTO.setStudentResultDTOS(studentResultDTOS);

        // set student achievements
        List<StudentAchievementsDTO> studentAchievementsDTOS =
                studentEntity.getStudentAchievementsEntities()
                        .stream()
                        .map(studentAchievementsEntity ->
                                modelMapper.map(
                                        studentAchievementsEntity,
                                        StudentAchievementsDTO.class
                                ))
                        .toList();
        studentDTO.setStudentAchievementsDTOS(studentAchievementsDTOS);

        // set student fee information
        List<StudentFeeDTO> studentFeeDTOS = new ArrayList<>();
        if(studentEntity.getStudentFeeEntities() != null && !studentEntity.getStudentFeeEntities().isEmpty()){
            for(StudentFeeEntity studentFeeEntity : studentEntity.getStudentFeeEntities()){
                StudentFeeDTO studentFeeDTO = modelMapper.map(studentFeeEntity, StudentFeeDTO.class);

                List<FeePaymentDTO> feePaymentDTOS = new ArrayList<>();
                if(studentFeeEntity.getFeePaymentEntities() != null && !studentFeeEntity.getFeePaymentEntities().isEmpty()){
                    for(FeePaymentEntity feePaymentEntity : studentFeeEntity.getFeePaymentEntities()){
                        FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                        feePaymentDTOS.add(feePaymentDTO);
                    }
                }
                studentFeeDTO.setFeePaymentDTOS(feePaymentDTOS);
                studentFeeDTOS.add(studentFeeDTO);
            }
        }
        studentDTO.setStudentFeeDTOS(studentFeeDTOS);

        return studentDTO;
    }

}
