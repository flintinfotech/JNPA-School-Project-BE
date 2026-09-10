package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerExamSubjectsDTO;
import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDTO;
import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDocumentDTO;
import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentResultDTO;
import com.flint.sample_be_springboot.entity.formerStudent.FormerExamSubjectsEntity;
import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentDocumentEntity;
import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentEntity;
import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentResultEntity;
import com.flint.sample_be_springboot.entity.student.ExamSubjectsEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.FormerStudentRepository.FormerStudentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FormerStudentServiceImpl extends BaseService implements FormerStudentService {


    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private FormerStudentRepository formerStudentRepository;

    @Override
    public FormerStudentDTO saveFormerStudent(FormerStudentDTO formerStudentDTO) {

        log.info("Enter into saveFormerStudent");

        // 1. Validate DTO
        if (formerStudentDTO == null) {
            throw new CustomException("Former student information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        // 2. Map DTO to Entity
        FormerStudentEntity formerStudentEntity = modelMapper.map(formerStudentDTO, FormerStudentEntity.class);
        formerStudentEntity.setAuditDetails(addAuditDetails(formerStudentEntity.getAuditDetails()));

        List<FormerStudentResultEntity> formerStudentResultEntities = new ArrayList<>();
        if (formerStudentDTO.getFormerStudentResultDTOS() != null && !formerStudentDTO.getFormerStudentResultDTOS().isEmpty()) {
            for (FormerStudentResultDTO formerStudentResultDTO : formerStudentDTO.getFormerStudentResultDTOS()) {

                FormerStudentResultEntity formerStudentResultEntity =
                        modelMapper.map(formerStudentResultDTO, FormerStudentResultEntity.class);

                // IMPORTANT: this is a NEW former-student result
                formerStudentResultEntity.setFormerResultId(null);

                formerStudentResultEntity.setFormerStudentEntity(formerStudentEntity);

                List<FormerExamSubjectsEntity> formerExamSubjectsEntities = new ArrayList<>();

                if (formerStudentResultDTO.getFormerExamSubjectsDTOS() != null) {

                    for (FormerExamSubjectsDTO subject :
                            formerStudentResultDTO.getFormerExamSubjectsDTOS()) {

                        FormerExamSubjectsEntity formerSubject =
                                modelMapper.map(subject, FormerExamSubjectsEntity.class);

                        // IMPORTANT: this is a NEW former exam subject
                        formerSubject.setFormerExamSubjectsId(null);

                        formerSubject.setFormerStudentResultEntity(formerStudentResultEntity);

                        formerExamSubjectsEntities.add(formerSubject);
                    }
                }

                formerStudentResultEntity.setFormerExamSubjectsEntities(formerExamSubjectsEntities);

                formerStudentResultEntities.add(formerStudentResultEntity);
            }
        }
        formerStudentEntity.setFormerStudentResultEntities(formerStudentResultEntities);

        // 4. Save former student
        FormerStudentEntity savedEntity = formerStudentRepository.save(formerStudentEntity);

        // 5. Map saved entity to DTO
        FormerStudentDTO savedDTO = modelMapper.map(savedEntity, FormerStudentDTO.class);
        log.info("Exit from saveFormerStudent");

        return savedDTO;
    }

//    @Override
//    public FormerStudentDTO getFormerStudent(Long formerStudentId) {
//        log.info("Enter into getFormerStudent");
//
//        // 1. Validate ID
//        if (formerStudentId == null) {
//            throw new CustomException("Former student ID cannot be null", HttpStatus.BAD_REQUEST);
//        }
//
//        // 2. Find former student
//        FormerStudentEntity formerStudentEntity = formerStudentRepository.findById(formerStudentId)
//                .orElseThrow(() -> new CustomException("Former student not found", HttpStatus.NOT_FOUND));
//
//        // 3. Map basic student information
//        FormerStudentDTO formerStudentDTO = modelMapper.map(formerStudentEntity, FormerStudentDTO.class);
//
//        // 5. Set student documents
//        List<FormerStudentDocumentDTO> documentDTOS = new ArrayList<>();
//
//        if (formerStudentEntity.getFormerStudentDocumentEntities() != null
//                && !formerStudentEntity.getFormerStudentDocumentEntities().isEmpty()) {
//
//            for (FormerStudentDocumentEntity documentEntity : formerStudentEntity.getFormerStudentDocumentEntities()) {
//
//                FormerStudentDocumentDTO documentDTO = modelMapper.map(documentEntity, FormerStudentDocumentDTO.class);
//
//                // Convert document byte[] to Base64
//                if (documentEntity.getDocument() != null) {
//                    documentDTO.setDocument((documentEntity.getDocument()));
//                }
//                documentDTOS.add(documentDTO);
//            }
//        }
//
//        formerStudentDTO.setFormerStudentDocuments(documentDTOS);
//
//        // 6. Set former student results
//        List<FormerStudentResultDTO> resultDTOS = new ArrayList<>();
//
//        if (formerStudentEntity.getFormerStudentResultEntities() != null
//                && !formerStudentEntity.getFormerStudentResultEntities().isEmpty()) {
//
//            for (FormerStudentResultEntity resultEntity : formerStudentEntity.getFormerStudentResultEntities()) {
//
//                FormerStudentResultDTO resultDTO = modelMapper.map(resultEntity, FormerStudentResultDTO.class);
//
//                List<FormerExamSubjectsDTO> formerExamSubjectsDTOS = new ArrayList<>();
//                for(FormerExamSubjectsEntity formerExamSubjectsEntity : resultEntity.getFormerExamSubjectsEntities()){
//                    FormerExamSubjectsDTO formerExamSubjectsDTO = modelMapper.map(formerExamSubjectsEntity, FormerExamSubjectsDTO.class);
//                    formerExamSubjectsDTO.setFormerResultId(resultDTO.getFormerResultId());
//
//                    formerExamSubjectsDTOS.add(formerExamSubjectsDTO);
//                }
//
//                resultDTO.setFormerExamSubjectsDTOS(formerExamSubjectsDTOS);
//
//                // Set former student ID
//                resultDTO.setFormerStudentId(formerStudentEntity.getFormerStudentId());
//            }
//        }
//        formerStudentDTO.setFormerStudentResultDTOS(resultDTOS);
//        log.info("Exit from getFormerStudent");
//
//        return formerStudentDTO;
//    }

    @Override
    public FormerStudentDTO getFormerStudent(Long formerStudentId) {
        log.info("Enter into getFormerStudent");

        if (formerStudentId == null) {
            throw new CustomException(
                    "Former student ID cannot be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        FormerStudentEntity formerStudentEntity =
                formerStudentRepository.findById(formerStudentId)
                        .orElseThrow(() -> new CustomException(
                                "Former student not found",
                                HttpStatus.NOT_FOUND
                        ));

        // Map basic former student information
        FormerStudentDTO formerStudentDTO =
                modelMapper.map(formerStudentEntity, FormerStudentDTO.class);

        // Documents
        List<FormerStudentDocumentDTO> documentDTOS = new ArrayList<>();

        if (formerStudentEntity.getFormerStudentDocumentEntities() != null) {

            for (FormerStudentDocumentEntity documentEntity :
                    formerStudentEntity.getFormerStudentDocumentEntities()) {

                FormerStudentDocumentDTO documentDTO =
                        modelMapper.map(
                                documentEntity,
                                FormerStudentDocumentDTO.class
                        );

                if (documentEntity.getDocument() != null) {
                    documentDTO.setDocument(documentEntity.getDocument());
                }

                documentDTOS.add(documentDTO);
            }
        }

        formerStudentDTO.setFormerStudentDocuments(documentDTOS);

        // Results
        List<FormerStudentResultDTO> resultDTOS = new ArrayList<>();

        if (formerStudentEntity.getFormerStudentResultEntities() != null) {

            for (FormerStudentResultEntity resultEntity :
                    formerStudentEntity.getFormerStudentResultEntities()) {

                // Manually map result to avoid ModelMapper ambiguity
                FormerStudentResultDTO resultDTO = new FormerStudentResultDTO();

                resultDTO.setFormerResultId(resultEntity.getFormerResultId());
                resultDTO.setFormerStudentId(
                        formerStudentEntity.getFormerStudentId()
                );
                resultDTO.setStandard(resultEntity.getStandard());
                resultDTO.setDivision(resultEntity.getDivision());
                resultDTO.setExamType(resultEntity.getExamType());
                resultDTO.setAcademicYear(resultEntity.getAcademicYear());
                resultDTO.setStartDate(resultEntity.getStartDate());
                resultDTO.setEndDate(resultEntity.getEndDate());
                resultDTO.setTotalMarks(resultEntity.getTotalMarks());
                resultDTO.setObtainedMarks(resultEntity.getObtainedMarks());
                resultDTO.setPercentage(resultEntity.getPercentage());
                resultDTO.setGrade(resultEntity.getGrade());
                resultDTO.setResultStatus(resultEntity.getResultStatus());
                resultDTO.setAuditDetails(resultEntity.getAuditDetails());

                // Subjects
                List<FormerExamSubjectsDTO> subjectDTOS = new ArrayList<>();

                if (resultEntity.getFormerExamSubjectsEntities() != null) {

                    for (FormerExamSubjectsEntity subjectEntity :
                            resultEntity.getFormerExamSubjectsEntities()) {

                        FormerExamSubjectsDTO subjectDTO =
                                new FormerExamSubjectsDTO();

                        subjectDTO.setExamSubjectsId(
                                subjectEntity.getFormerExamSubjectsId()
                        );
                        subjectDTO.setFormerResultId(
                                resultEntity.getFormerResultId()
                        );
                        subjectDTO.setSubjectName(
                                subjectEntity.getSubjectName()
                        );
                        subjectDTO.setMaximumMarks(
                                subjectEntity.getMaximumMarks()
                        );
                        subjectDTO.setObtainedMarks(
                                subjectEntity.getObtainedMarks()
                        );
                        subjectDTO.setStatus(
                                subjectEntity.getStatus()
                        );
                        subjectDTO.setAuditDetails(
                                subjectEntity.getAuditDetails()
                        );

                        subjectDTOS.add(subjectDTO);
                    }
                }

                resultDTO.setFormerExamSubjectsDTOS(subjectDTOS);

                resultDTOS.add(resultDTO);
            }
        }

        formerStudentDTO.setFormerStudentResultDTOS(resultDTOS);

        log.info("Exit from getFormerStudent");

        return formerStudentDTO;
    }



    @Override
    public FormerStudentDTO updateFormerStudent(FormerStudentDTO formerStudentDTO) {

        log.info("Enter into updateFormerStudent");

        if (formerStudentDTO == null) {
            throw new CustomException("Former student information cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (formerStudentDTO.getFormerStudentId() == null) {
            throw new CustomException("Former student ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        FormerStudentEntity existingEntity = formerStudentRepository.findById(formerStudentDTO.getFormerStudentId())
                .orElseThrow(() -> new CustomException("Former student not found", HttpStatus.NOT_FOUND));

        // Update basic fields
        existingEntity.setFirstName(formerStudentDTO.getFirstName());
        existingEntity.setLastName(formerStudentDTO.getLastName());
        existingEntity.setGender(formerStudentDTO.getGender());
        existingEntity.setDOB(formerStudentDTO.getDOB());
        existingEntity.setAadhaarCard(formerStudentDTO.getAadhaarCard());
        existingEntity.setPhone(formerStudentDTO.getPhone());
        existingEntity.setAddress(formerStudentDTO.getAddress());
        existingEntity.setBloodGroup(formerStudentDTO.getBloodGroup());
        existingEntity.setCategory(formerStudentDTO.getCategory());
        existingEntity.setReligion(formerStudentDTO.getReligion());
        existingEntity.setCaste(formerStudentDTO.getCaste());
        existingEntity.setNationality(formerStudentDTO.getNationality());
        existingEntity.setStatus(formerStudentDTO.getStatus());
        existingEntity.setAdmissionNo(formerStudentDTO.getAdmissionNo());
        existingEntity.setPaymentStatus(formerStudentDTO.getPaymentStatus());
        existingEntity.setTotalFeeAmount(formerStudentDTO.getTotalFeeAmount());
        existingEntity.setPendingFeeAmount(formerStudentDTO.getPendingFeeAmount());

        existingEntity.setAuditDetails(addAuditDetails(existingEntity.getAuditDetails()));

        // Update documents
        if (formerStudentDTO.getFormerStudentDocuments() != null) {

            List<FormerStudentDocumentEntity> existingDocuments =
                    existingEntity.getFormerStudentDocumentEntities();

            if (existingDocuments == null) {
                existingDocuments = new ArrayList<>();
                existingEntity.setFormerStudentDocumentEntities(existingDocuments);
            }

            // IDs of documents which are coming from frontend
            Set<Long> incomingDocumentIds = formerStudentDTO
                    .getFormerStudentDocuments()
                    .stream()
                    .map(FormerStudentDocumentDTO::getFormerStudentDocumentId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Remove documents which are not present in request
            existingDocuments.removeIf(document ->
                    document.getFormerStudentDocumentId() != null
                            && !incomingDocumentIds.contains(
                            document.getFormerStudentDocumentId()));

            // Add / update documents
            for (FormerStudentDocumentDTO documentDTO : formerStudentDTO.getFormerStudentDocuments()) {

                FormerStudentDocumentEntity documentEntity;

                // Existing document
                if (documentDTO.getFormerStudentDocumentId() != null) {
                    documentEntity = existingDocuments.stream()
                            .filter(document ->
                                    document.getFormerStudentDocumentId().equals(documentDTO.getFormerStudentDocumentId())
                            )
                            .findFirst()
                            .orElseThrow(() -> new CustomException("Document not found: " + documentDTO.getFormerStudentDocumentId(), HttpStatus.NOT_FOUND));

                } else {

                    // New document
                    documentEntity = new FormerStudentDocumentEntity();
                    documentEntity.setFormerStudentEntity(existingEntity);
                    documentEntity.setAuditDetails(addAuditDetails(null));

                    existingDocuments.add(documentEntity);
                }

                // Update document fields
                documentEntity.setDocumentName(documentDTO.getDocumentName());
                documentEntity.setDocumentType(documentDTO.getDocumentType());
                documentEntity.setAcademicYear(documentDTO.getAcademicYear());
                documentEntity.setStandard(documentDTO.getStandard());
                documentEntity.setDocumentDate(documentDTO.getDocumentDate());
                documentEntity.setDocumentStatus(documentDTO.getDocumentStatus());
                documentEntity.setDocumentStatus(documentDTO.getDocumentStatus());
                documentEntity.setCollectedBy(documentDTO.getCollectedBy());
                documentEntity.setCollectedRelation(documentDTO.getCollectedRelation());
                documentEntity.setRemark(documentDTO.getRemark());

                if (documentDTO.getUploadDate() != null) {
                    documentEntity.setUploadDate(documentDTO.getUploadDate());
                } else if (documentEntity.getUploadDate() == null) {
                    documentEntity.setUploadDate(LocalDate.now());
                }

                if (documentDTO.getCollectedDate() != null) {
                    documentEntity.setCollectedDate(documentDTO.getCollectedDate());
                } else if (documentEntity.getCollectedDate() == null) {
                    documentEntity.setCollectedDate(LocalDate.now());
                }

                // Update document file only when new file is provided
                if (documentDTO.getDocument() != null && !documentDTO.getDocument().isEmpty()) {
                    documentEntity.setDocument(Base64.getDecoder().decode(documentDTO.getDocument()));
                }else{
                    documentEntity.setDocument(null);
                }

                // Update audit
                if (documentEntity.getFormerStudentDocumentId() != null) {
                    documentEntity.setAuditDetails(addAuditDetails(documentEntity.getAuditDetails()));
                }
            }
        }

        // Save updated entity
        FormerStudentEntity savedEntity = formerStudentRepository.save(existingEntity);

        // Convert entity to DTO
        FormerStudentDTO savedDTO = modelMapper.map(savedEntity, FormerStudentDTO.class);

        // Map documents
        List<FormerStudentDocumentDTO> documentDTOS = new ArrayList<>();

        if (savedEntity.getFormerStudentDocumentEntities() != null && !savedEntity.getFormerStudentDocumentEntities().isEmpty()) {

            for (FormerStudentDocumentEntity documentEntity : savedEntity.getFormerStudentDocumentEntities()) {

                FormerStudentDocumentDTO documentDTO = modelMapper.map(documentEntity, FormerStudentDocumentDTO.class);

                if (documentEntity.getDocument() != null) {
                    documentDTO.setDocument((documentEntity.getDocument()));
                }

                documentDTOS.add(documentDTO);
            }
        }

        savedDTO.setFormerStudentDocuments(documentDTOS);
        log.info("Exit from updateFormerStudent");

        return savedDTO;
    }

    @Override
    public String deleteFormerStudent(Long formerStudentId) {
        log.info("Enter into deleteFormerStudent");

        // 1. Validate ID
        if (formerStudentId == null) {
            throw new CustomException("Former student ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        // 2. Find former student
        FormerStudentEntity formerStudentEntity = formerStudentRepository.findById(formerStudentId)
                .orElseThrow(() -> new CustomException("Former student not found", HttpStatus.NOT_FOUND));

        // 3. Delete former student
        formerStudentRepository.delete(formerStudentEntity);

        log.info("Exit from deleteFormerStudent");

        return "Former student deleted successfully";
    }

    @Override
    public Map<String, Object> getAllFormerStudentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllFormerStudentByFilter");

        Page<FormerStudentEntity> formerStudentEntityPage;
        List<FormerStudentEntity> formerStudentEntities;
        long totalElement;

        CustomQuerySpecification<FormerStudentEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        // 1. Fetch data
        if (paginate) {

            formerStudentEntityPage = formerStudentRepository.findAll(customQuerySpecification, pageable);
            formerStudentEntities = formerStudentEntityPage.getContent();
            totalElement = formerStudentEntityPage.getTotalElements();

        } else {
            formerStudentEntities = formerStudentRepository.findAll(customQuerySpecification);
            totalElement = formerStudentEntities.size();
        }

        // 2. Convert Entity to DTO
        List<FormerStudentDTO> formerStudentDTOS = new ArrayList<>();

        for (FormerStudentEntity formerStudentEntity : formerStudentEntities) {

            FormerStudentDTO formerStudentDTO = modelMapper.map(formerStudentEntity, FormerStudentDTO.class);

            // 4. Map documents
            List<FormerStudentDocumentDTO> documentDTOS = new ArrayList<>();

            if (formerStudentEntity.getFormerStudentDocumentEntities() != null
                    && !formerStudentEntity.getFormerStudentDocumentEntities().isEmpty()) {

                for (FormerStudentDocumentEntity documentEntity : formerStudentEntity.getFormerStudentDocumentEntities()) {

                    FormerStudentDocumentDTO documentDTO = modelMapper.map(documentEntity, FormerStudentDocumentDTO.class);

                    if (documentEntity.getDocument() != null) {

                        documentDTO.setDocument((documentEntity.getDocument()));
                    }

                    documentDTOS.add(documentDTO);
                }
            }

            formerStudentDTO.setFormerStudentDocuments(documentDTOS);

            // 5. Map results
            List<FormerStudentResultDTO> resultDTOS = new ArrayList<>();

            if (formerStudentEntity.getFormerStudentResultEntities() != null) {

                for (FormerStudentResultEntity resultEntity :
                        formerStudentEntity.getFormerStudentResultEntities()) {

                    // Manually map result to avoid ModelMapper ambiguity
                    FormerStudentResultDTO resultDTO = new FormerStudentResultDTO();

                    resultDTO.setFormerResultId(resultEntity.getFormerResultId());
                    resultDTO.setFormerStudentId(
                            formerStudentEntity.getFormerStudentId()
                    );
                    resultDTO.setStandard(resultEntity.getStandard());
                    resultDTO.setDivision(resultEntity.getDivision());
                    resultDTO.setExamType(resultEntity.getExamType());
                    resultDTO.setAcademicYear(resultEntity.getAcademicYear());
                    resultDTO.setStartDate(resultEntity.getStartDate());
                    resultDTO.setEndDate(resultEntity.getEndDate());
                    resultDTO.setTotalMarks(resultEntity.getTotalMarks());
                    resultDTO.setObtainedMarks(resultEntity.getObtainedMarks());
                    resultDTO.setPercentage(resultEntity.getPercentage());
                    resultDTO.setGrade(resultEntity.getGrade());
                    resultDTO.setResultStatus(resultEntity.getResultStatus());
                    resultDTO.setAuditDetails(resultEntity.getAuditDetails());

                    // Subjects
                    List<FormerExamSubjectsDTO> subjectDTOS = new ArrayList<>();

                    if (resultEntity.getFormerExamSubjectsEntities() != null) {

                        for (FormerExamSubjectsEntity subjectEntity :
                                resultEntity.getFormerExamSubjectsEntities()) {

                            FormerExamSubjectsDTO subjectDTO =
                                    new FormerExamSubjectsDTO();

                            subjectDTO.setExamSubjectsId(
                                    subjectEntity.getFormerExamSubjectsId()
                            );
                            subjectDTO.setFormerResultId(
                                    resultEntity.getFormerResultId()
                            );
                            subjectDTO.setSubjectName(
                                    subjectEntity.getSubjectName()
                            );
                            subjectDTO.setMaximumMarks(
                                    subjectEntity.getMaximumMarks()
                            );
                            subjectDTO.setObtainedMarks(
                                    subjectEntity.getObtainedMarks()
                            );
                            subjectDTO.setStatus(
                                    subjectEntity.getStatus()
                            );
                            subjectDTO.setAuditDetails(
                                    subjectEntity.getAuditDetails()
                            );

                            subjectDTOS.add(subjectDTO);
                        }
                    }

                    resultDTO.setFormerExamSubjectsDTOS(subjectDTOS);

                    resultDTOS.add(resultDTO);
                }
            }

            formerStudentDTO.setFormerStudentResultDTOS(resultDTOS);

            formerStudentDTOS.add(formerStudentDTO);
        }

        // 6. Prepare response
        Map<String, Object> response = new HashMap<>();

        response.put("Former Student Data", formerStudentDTOS);
        response.put("Total Element", totalElement);

        if (paginate) {
            response.put("Page Number", pageable.getPageNumber());
            response.put("Page Size", pageable.getPageSize());
            response.put("Total Pages",
                    (int) Math.ceil(
                            (double) totalElement /
                                    pageable.getPageSize()
                    ));
        }

        log.info("Exit from getAllFormerStudentByFilter");

        return response;
    }
}
