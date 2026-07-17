package com.flint.sample_be_springboot.service.websiteModuleService;

import com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam.ExamDTO;
import com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam.ExamNoticeDTO;
import com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam.ExamResultDTO;
import com.flint.sample_be_springboot.dto.websiteModuleDTOS.exam.ToppersDTO;
import com.flint.sample_be_springboot.entity.AuditDetails;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.exam.ExamEntity;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.exam.ExamNoticeEntity;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.exam.ExamResultEntity;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.exam.ToppersEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.websiteModuleRepository.ExamRepository;
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
public class ExamServiceImpl extends BaseService implements ExamService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    ExamRepository examRepository;

    @Override
    public ExamDTO saveExam(ExamDTO examDTO) {
        log.info("Enter into saveExam");

        ExamEntity isExist = examRepository.findByClassRoomNameAndAcademicYearNameAndMedium
                (examDTO.getClassRoomName(), examDTO.getAcademicYearName(), examDTO.getMedium());

        if (isExist != null) {
            throw new CustomException("Exam/Notice details are already exists", HttpStatus.PRECONDITION_FAILED);
        }

        ExamEntity examEntity = modelMapper.map(examDTO, ExamEntity.class);
        examEntity.setAuditDetails(addAuditDetails(examEntity.getAuditDetails()));

        // set exam result entities
        List<ExamResultEntity> examResultEntities = new ArrayList<>();
        if(examDTO.getExamResultDTOS() != null && !examDTO.getExamResultDTOS().isEmpty()){
            for(ExamResultDTO examResultDTO : examDTO.getExamResultDTOS()){
                ExamResultEntity examResultEntity = modelMapper.map(examResultDTO, ExamResultEntity.class);
                examResultEntity.setAuditDetails(addAuditDetails(examEntity.getAuditDetails()));
                examResultEntity.setExamEntity(examEntity);
                if(examResultDTO.getResultData() != null){
                    examResultEntity.setResultData(Base64.getDecoder().decode(examResultDTO.getResultData()));
                }
                examResultEntities.add(examResultEntity);
            }
        }
        examEntity.setExamResults(examResultEntities);

        // set exam notice entities
        List<ExamNoticeEntity> examNoticeEntities = new ArrayList<>();
        if(examDTO.getExamNoticeDTOS() != null && !examDTO.getExamNoticeDTOS().isEmpty()){
            for(ExamNoticeDTO examNoticeDTO : examDTO.getExamNoticeDTOS()){
                ExamNoticeEntity examNoticeEntity = modelMapper.map(examNoticeDTO, ExamNoticeEntity.class);
                examNoticeEntity.setAuditDetails(addAuditDetails(examEntity.getAuditDetails()));
                examNoticeEntity.setExamEntity(examEntity);
                if(examNoticeDTO.getNoticeData() != null){
                    examNoticeEntity.setNoticeData(Base64.getDecoder().decode(examNoticeDTO.getNoticeData()));
                }
                examNoticeEntities.add(examNoticeEntity);
            }
        }
        examEntity.setExamNotices(examNoticeEntities);

        // set toppers data
        List<ToppersEntity> toppersEntities = new ArrayList<>();
        if(examDTO.getToppersDTOS() != null && !examDTO.getToppersDTOS().isEmpty()){
            for(ToppersDTO toppersDTO : examDTO.getToppersDTOS()){
                ToppersEntity toppersEntity = modelMapper.map(toppersDTO, ToppersEntity.class);

                if(toppersDTO.getStudentImage() != null){
                    toppersEntity.setStudentImage(Base64.getDecoder().decode(toppersDTO.getStudentImage()));
                }

                toppersEntity.setAuditDetails(addAuditDetails(examEntity.getAuditDetails()));
                toppersEntity.setExamEntity(examEntity);
                toppersEntities.add(toppersEntity);
            }
        }
        examEntity.setToppersEntities(toppersEntities);

        ExamEntity savedExamEntity = examRepository.save(examEntity);

        // set result entity DTOs
        List<ExamResultDTO> examResultDTOS = new ArrayList<>();
        if(savedExamEntity.getExamResults() != null && !savedExamEntity.getExamResults().isEmpty()){
            for(ExamResultEntity examResultEntity : savedExamEntity.getExamResults()){
                ExamResultDTO examResultDTO = modelMapper.map(examResultEntity, ExamResultDTO.class);
                examResultDTO.setExamId(savedExamEntity.getExamId());
                examResultDTOS.add(examResultDTO);
            }
        }

        // set result notice DTOs
        List<ExamNoticeDTO> examNoticeDTOS = new ArrayList<>();
        if (examEntity.getExamNotices() != null && !examEntity.getExamNotices().isEmpty()) {
            for (ExamNoticeEntity examNoticeEntity : examEntity.getExamNotices()) {
                ExamNoticeDTO examNoticeDTO = modelMapper.map(examNoticeEntity, ExamNoticeDTO.class);
                examNoticeDTO.setExamId(savedExamEntity.getExamId());
                examNoticeDTOS.add(examNoticeDTO);
            }
        }

        // set toppers DTOs
        List<ToppersDTO> toppersDTOS = new ArrayList<>();
        if (examEntity.getToppersEntities() != null && !examEntity.getToppersEntities().isEmpty()) {
            for (ToppersEntity toppersEntity : examEntity.getToppersEntities()) {
                ToppersDTO toppersDTO = modelMapper.map(toppersEntity, ToppersDTO.class);
                toppersDTOS.add(toppersDTO);
            }
        }

        ExamDTO savedDto = modelMapper.map(savedExamEntity, ExamDTO.class);
        savedDto.setExamResultDTOS(examResultDTOS);
        savedDto.setExamNoticeDTOS(examNoticeDTOS);
        savedDto.setToppersDTOS(toppersDTOS);

        log.info("Exit from saveExam");
        return savedDto;
    }

    @Override
    public ExamDTO updateExam(ExamDTO examDTO) {
        log.info("Enter into updateExam");

        ExamEntity existingEntity = examRepository.findById(examDTO.getExamId())
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        examRepository.findByClassRoomNameAndAcademicYearNameAndMediumAndExamIdNot
                        (examDTO.getClassRoomName(), examDTO.getAcademicYearName(), examDTO.getMedium(), examDTO.getExamId())
                .ifPresent(entity -> {
                    throw new CustomException("Exam/Notice details are already exists", HttpStatus.PRECONDITION_FAILED);
                });

        AuditDetails auditDetails = existingEntity.getAuditDetails();

        modelMapper.map(examDTO, existingEntity);
        existingEntity.setAuditDetails(addAuditDetails(auditDetails));

        // set exam result entities
        // existing entities
        Set<Long> requestIds = examDTO.getExamResultDTOS().stream()
                .map(ExamResultDTO::getExamResultId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getExamResults().removeIf(result ->
                result.getExamResultId() != null &&
                        !requestIds.contains(result.getExamResultId()));

        Map<Long, ExamResultEntity> examMap = existingEntity.getExamResults().stream()
                .collect(Collectors.toMap(
                        ExamResultEntity::getExamResultId,
                        e -> e
                ));

        if (examDTO.getExamResultDTOS() != null && !examDTO.getExamResultDTOS().isEmpty()) {
            for (ExamResultDTO examResultDTO : examDTO.getExamResultDTOS()) {
                ExamResultEntity examResultEntity;

                // update existing result
                if (examResultDTO.getExamResultId() != null && examMap.containsKey(examResultDTO.getExamResultId())) {

                    examResultEntity = examMap.get(examResultDTO.getExamResultId());

                    AuditDetails entityAuditDetails = examResultEntity.getAuditDetails();
                    modelMapper.map(examResultDTO, examResultEntity);

                    examResultEntity.setAuditDetails(entityAuditDetails);

                    examResultEntity.setExamEntity(existingEntity);
                    if (examResultDTO.getResultData() != null) {
                        examResultEntity.setResultData(Base64.getDecoder().decode(examResultDTO.getResultData()));
                    } else {
                        examResultEntity.setResultData(null);
                    }
                }
                // add new result
                else {
                    examResultEntity = modelMapper.map(examResultDTO, ExamResultEntity.class);
                    examResultEntity.setExamEntity(existingEntity);
                    if (examResultDTO.getResultData() != null) {
                        examResultEntity.setResultData(Base64.getDecoder().decode(examResultDTO.getResultData()));
                    }
                    examResultEntity.setAuditDetails(addAuditDetails(examResultEntity.getAuditDetails()));

                    existingEntity.getExamResults().add(examResultEntity);
                }
            }
        }

        // Set Exam Notice entities
        Set<Long> requestNoticeIds = examDTO.getExamNoticeDTOS().stream()
                .map(ExamNoticeDTO::getExamNoticeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getExamNotices().removeIf(notice ->
                notice.getExamNoticeId() != null &&
                        !requestNoticeIds.contains(notice.getExamNoticeId()));

        Map<Long, ExamNoticeEntity> noticeMap = existingEntity.getExamNotices().stream()
                .collect(Collectors.toMap(
                        ExamNoticeEntity::getExamNoticeId,
                        Function.identity()
                ));

        if (examDTO.getExamNoticeDTOS() != null && !examDTO.getExamNoticeDTOS().isEmpty()) {
            for (ExamNoticeDTO examNoticeDTO : examDTO.getExamNoticeDTOS()) {

                ExamNoticeEntity examNoticeEntity;

                // Update existing
                if (examNoticeDTO.getExamNoticeId() != null &&
                        noticeMap.containsKey(examNoticeDTO.getExamNoticeId())) {

                    examNoticeEntity = noticeMap.get(examNoticeDTO.getExamNoticeId());

                    AuditDetails auditDetails1 = examNoticeEntity.getAuditDetails();

                    modelMapper.map(examNoticeDTO, examNoticeEntity);

                    examNoticeEntity.setAuditDetails(auditDetails1);
                    examNoticeEntity.setExamEntity(existingEntity);

                    if (examNoticeDTO.getNoticeData() != null) {
                        examNoticeEntity.setNoticeData(Base64.getDecoder().decode(examNoticeDTO.getNoticeData()));
                    } else {
                        examNoticeEntity.setNoticeData(null);
                    }

                }
                // Add new
                else {

                    examNoticeEntity = modelMapper.map(examNoticeDTO, ExamNoticeEntity.class);

                    examNoticeEntity.setExamEntity(existingEntity);

                    if (examNoticeDTO.getNoticeData() != null) {
                        examNoticeEntity.setNoticeData(Base64.getDecoder().decode(examNoticeDTO.getNoticeData()));
                    }

                    examNoticeEntity.setAuditDetails(addAuditDetails(examNoticeEntity.getAuditDetails()));

                    existingEntity.getExamNotices().add(examNoticeEntity);
                }
            }
        }

        // Set Toppers entities
        Set<Long> requestTopperIds = examDTO.getToppersDTOS().stream()
                .map(ToppersDTO::getTopperId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEntity.getToppersEntities().removeIf(topper ->
                topper.getTopperId() != null &&
                        !requestTopperIds.contains(topper.getTopperId()));

        Map<Long, ToppersEntity> topperMap = existingEntity.getToppersEntities().stream()
                .collect(Collectors.toMap(
                        ToppersEntity::getTopperId,
                        Function.identity()
                ));

        if (examDTO.getToppersDTOS() != null && !examDTO.getToppersDTOS().isEmpty()) {

            for (ToppersDTO toppersDTO : examDTO.getToppersDTOS()) {

                ToppersEntity toppersEntity;

                // Update existing
                if (toppersDTO.getTopperId() != null &&
                        topperMap.containsKey(toppersDTO.getTopperId())) {

                    toppersEntity = topperMap.get(toppersDTO.getTopperId());

                    if(toppersDTO.getStudentImage() != null){
                        toppersEntity.setStudentImage(Base64.getDecoder().decode(toppersDTO.getStudentImage()));
                    }else{
                        toppersEntity.setStudentImage(null);
                    }

                    AuditDetails auditDetails2 = toppersEntity.getAuditDetails();

                    modelMapper.map(toppersDTO, toppersEntity);

                    toppersEntity.setAuditDetails(auditDetails2);
                    toppersEntity.setExamEntity(existingEntity);

                }
                // Add new
                else {

                    toppersEntity = modelMapper.map(toppersDTO, ToppersEntity.class);

                    if(toppersDTO.getStudentImage() != null){
                        toppersEntity.setStudentImage(Base64.getDecoder().decode(toppersDTO.getStudentImage()));
                    }

                    toppersEntity.setExamEntity(existingEntity);

                    toppersEntity.setAuditDetails(addAuditDetails(toppersEntity.getAuditDetails()));

                    existingEntity.getToppersEntities().add(toppersEntity);
                }
            }
        }

        ExamEntity updatedExamEntity = examRepository.save(existingEntity);

        // set result entity DTOs
        List<ExamResultDTO> examResultDTOS = new ArrayList<>();
        if (updatedExamEntity.getExamResults() != null && !updatedExamEntity.getExamResults().isEmpty()) {
            for (ExamResultEntity examResultEntity : updatedExamEntity.getExamResults()) {
                ExamResultDTO examResultDTO = modelMapper.map(examResultEntity, ExamResultDTO.class);
                examResultDTO.setExamId(updatedExamEntity.getExamId());
                examResultDTOS.add(examResultDTO);
            }
        }

        // set result notice DTOs
        List<ExamNoticeDTO> examNoticeDTOS = new ArrayList<>();
        if (updatedExamEntity.getExamNotices() != null && !updatedExamEntity.getExamNotices().isEmpty()) {
            for (ExamNoticeEntity examNoticeEntity : updatedExamEntity.getExamNotices()) {
                ExamNoticeDTO examNoticeDTO = modelMapper.map(examNoticeEntity, ExamNoticeDTO.class);
                examNoticeDTO.setExamId(updatedExamEntity.getExamId());
                examNoticeDTOS.add(examNoticeDTO);
            }
        }

        // set toppers DTOs
        List<ToppersDTO> toppersDTOS = new ArrayList<>();
        if (updatedExamEntity.getToppersEntities() != null && !updatedExamEntity.getToppersEntities().isEmpty()) {
            for (ToppersEntity toppersEntity : updatedExamEntity.getToppersEntities()) {
                ToppersDTO toppersDTO = modelMapper.map(toppersEntity, ToppersDTO.class);
                toppersDTOS.add(toppersDTO);
            }
        }

        ExamDTO updatedDto = modelMapper.map(updatedExamEntity, ExamDTO.class);
        updatedDto.setExamResultDTOS(examResultDTOS);
        updatedDto.setExamNoticeDTOS(examNoticeDTOS);
        updatedDto.setToppersDTOS(toppersDTOS);

        log.info("Exit from updateExam");
        return updatedDto;
    }

    @Override
    public ExamDTO getExamById(Long examId) {
        log.info("Enter into getExamById");

        ExamEntity examEntity = examRepository.findById(examId)
                .orElseThrow(() -> new CustomException("Exam/Notice details not found", HttpStatus.NOT_FOUND));

        ExamDTO examDTO = modelMapper.map(examEntity, ExamDTO.class);

        // set result entity DTOs
        List<ExamResultDTO> examResultDTOS = new ArrayList<>();
        if (examEntity.getExamResults() != null && !examEntity.getExamResults().isEmpty()) {
            for (ExamResultEntity examResultEntity : examEntity.getExamResults()) {
                ExamResultDTO examResultDTO = modelMapper.map(examResultEntity, ExamResultDTO.class);
                examResultDTO.setExamId(examEntity.getExamId());
                examResultDTOS.add(examResultDTO);
            }
        }

        // set result notice DTOs
        List<ExamNoticeDTO> examNoticeDTOS = new ArrayList<>();
        if (examEntity.getExamNotices() != null && !examEntity.getExamNotices().isEmpty()) {
            for (ExamNoticeEntity examNoticeEntity : examEntity.getExamNotices()) {
                ExamNoticeDTO examNoticeDTO = modelMapper.map(examNoticeEntity, ExamNoticeDTO.class);
                examNoticeDTO.setExamId(examEntity.getExamId());
                examNoticeDTOS.add(examNoticeDTO);
            }
        }

        // set toppers DTOs
        List<ToppersDTO> toppersDTOS = new ArrayList<>();
        if (examEntity.getToppersEntities() != null && !examEntity.getToppersEntities().isEmpty()) {
            for (ToppersEntity toppersEntity : examEntity.getToppersEntities()) {
                ToppersDTO toppersDTO = modelMapper.map(toppersEntity, ToppersDTO.class);
                toppersDTOS.add(toppersDTO);
            }
        }

        examDTO.setExamResultDTOS(examResultDTOS);
        examDTO.setExamNoticeDTOS(examNoticeDTOS);
        examDTO.setToppersDTOS(toppersDTOS);

        log.info("Exit from getExamById");
        return examDTO;
    }

    @Override
    public String deleteExam(Long examId) {
        log.info("Enter into deleteExam");

        ExamEntity examEntity = examRepository.findById(examId)
                .orElseThrow(() -> new CustomException("Exam/Notice details not found", HttpStatus.NOT_FOUND));

        // validations

        examRepository.delete(examEntity);

        log.info("Exit from deleteExam");
        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllExamsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllExamsByFilter");

        List<ExamEntity> examEntities;
        Page<ExamEntity> examEntityPage;
        long totalElements;

        CustomQuerySpecification<ExamEntity> customQuerySpecification =
                CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            examEntityPage = examRepository.findAll(customQuerySpecification, pageable);
            examEntities = examEntityPage.getContent();
            totalElements = examEntityPage.getTotalElements();
        } else {
            examEntities = examRepository.findAll(customQuerySpecification);
            totalElements = examEntities.size();
        }

        List<ExamDTO> examDTOS = examEntities.stream()
                .map(e -> {
                    ExamDTO examDTO = modelMapper.map(e, ExamDTO.class);

                    // set result entity DTOs
                    List<ExamResultDTO> examResultDTOS = new ArrayList<>();
                    if (e.getExamResults() != null && !e.getExamResults().isEmpty()) {
                        for (ExamResultEntity examResultEntity : e.getExamResults()) {
                            ExamResultDTO examResultDTO = modelMapper.map(examResultEntity, ExamResultDTO.class);
                            examResultDTO.setExamId(e.getExamId());
                            examResultDTOS.add(examResultDTO);
                        }
                    }

                    // set result notice DTOs
                    List<ExamNoticeDTO> examNoticeDTOS = new ArrayList<>();
                    if (e.getExamNotices() != null && !e.getExamNotices().isEmpty()) {
                        for (ExamNoticeEntity examNoticeEntity : e.getExamNotices()) {
                            ExamNoticeDTO examNoticeDTO = modelMapper.map(examNoticeEntity, ExamNoticeDTO.class);
                            examNoticeDTO.setExamId(e.getExamId());
                            examNoticeDTOS.add(examNoticeDTO);
                        }
                    }

                    // set toppers DTOs
                    List<ToppersDTO> toppersDTOS = new ArrayList<>();
                    if (e.getToppersEntities() != null && !e.getToppersEntities().isEmpty()) {
                        for (ToppersEntity toppersEntity : e.getToppersEntities()) {
                            ToppersDTO toppersDTO = modelMapper.map(toppersEntity, ToppersDTO.class);
                            toppersDTOS.add(toppersDTO);
                        }
                    }

                    examDTO.setExamResultDTOS(examResultDTOS);
                    examDTO.setExamNoticeDTOS(examNoticeDTOS);
                    examDTO.setToppersDTOS(toppersDTOS);

                    return examDTO;
                }).collect(Collectors.toList());

        log.info("Exit from getAllExamsByFilter");
        Map<String, Object> map = new HashMap<>();
        map.put("examDTOS", examDTOS);
        map.put("total elements", totalElements);
        return map;
    }
}
