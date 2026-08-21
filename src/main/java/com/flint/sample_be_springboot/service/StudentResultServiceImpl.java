package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.ExamSubjectsDTO;
import com.flint.sample_be_springboot.dto.student.StudentResultDTO;
import com.flint.sample_be_springboot.entity.student.ExamSubjectsEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.entity.student.StudentResultEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.student.ExamSubjectsRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.repository.student.StudentResultRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentResultServiceImpl extends BaseService implements StudentResultService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private StudentResultRepository studentResultRepository;
    @Autowired
    private ExamSubjectsRepository examSubjectsRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentResultDTO saveStudentResult(StudentResultDTO studentResultDTO) {
        log.info("Enter into saveStudentResult");

        if (studentResultDTO == null) {
            throw new CustomException("Student result info can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentEntity studentEntity = studentRepository.findById(studentResultDTO.getStudentId())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentResultEntity studentResultEntity = modelMapper.map(studentResultDTO, StudentResultEntity.class);
        studentResultEntity.setAuditDetails(addAuditDetails(studentResultEntity.getAuditDetails()));
        studentResultEntity.setStudentEntity(studentEntity);

        List<ExamSubjectsEntity> examSubjectsEntities = new ArrayList<>();
        for (ExamSubjectsDTO examSubjectsDTO : studentResultDTO.getExamSubjectsDTOS()) {

            ExamSubjectsEntity examSubjectsEntity = modelMapper.map(examSubjectsDTO, ExamSubjectsEntity.class);
            examSubjectsEntity.setStudentResult(studentResultEntity);
            examSubjectsEntities.add(examSubjectsEntity);

        }
        studentResultEntity.setExamSubjectsEntities(examSubjectsEntities);

        // saving the studenResultEntity
        StudentResultEntity savedResult = studentResultRepository.save(studentResultEntity);

        StudentResultDTO resultDTO = modelMapper.map(savedResult, StudentResultDTO.class);
        resultDTO.setStudentId(savedResult.getStudentEntity().getStudentId());

        List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
        if (savedResult.getExamSubjectsEntities() != null && !savedResult.getExamSubjectsEntities().isEmpty()) {
            for (ExamSubjectsEntity examSubjectsEntity : savedResult.getExamSubjectsEntities()) {
                ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                examSubjectsDTO.setResultId(resultDTO.getResultId());
                examSubjectsDTOS.add(examSubjectsDTO);
            }
        }
        resultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

        log.info("Exit from saveStudentResult");

        return resultDTO;

    }


    @Override
    public StudentResultDTO getStudentResultByStudentId(Long studentId) {
        log.info("Enter into getStudentResultById");

        StudentResultEntity studentResultEntity = studentResultRepository.findByStudentEntity_StudentId(studentId)
                .orElseThrow(() -> new CustomException("Student result is not available", HttpStatus.NOT_FOUND));

        StudentResultDTO resultDTO = modelMapper.map(studentResultEntity, StudentResultDTO.class);
        resultDTO.setStudentId(studentResultEntity.getStudentEntity().getStudentId());

        List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
        if (studentResultEntity.getExamSubjectsEntities() != null && !studentResultEntity.getExamSubjectsEntities().isEmpty()) {
            for (ExamSubjectsEntity examSubjectsEntity : studentResultEntity.getExamSubjectsEntities()) {
                ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                examSubjectsDTO.setResultId(resultDTO.getResultId());
                examSubjectsDTOS.add(examSubjectsDTO);
            }
        }
        resultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

        log.info("Exit from getStudentResultById");

        return resultDTO;
    }

    @Override
    public StudentResultDTO updateStudentResult(StudentResultDTO studentResultDTO) {
        log.info("Enter into updateStudentResult");

        StudentResultEntity existingStudentResultEntity = studentResultRepository.findById(studentResultDTO.getResultId())
                .orElseThrow(() -> new CustomException("Student result not found", HttpStatus.NOT_FOUND));

        modelMapper.map(existingStudentResultEntity, existingStudentResultEntity);

        // collect incoming Ids
        Set<Long> incomingIds = studentResultDTO.getExamSubjectsDTOS().stream()
                .map(ExamSubjectsDTO::getExamSubjectsId)
                .collect(Collectors.toSet());

        // remove deleted exam subjects
        existingStudentResultEntity.getExamSubjectsEntities().removeIf(e ->
                e.getExamSubjectsId() != null && !incomingIds.contains(e.getExamSubjectsId()));

        // existing exam result map
        Map<Long, ExamSubjectsEntity> map = existingStudentResultEntity.getExamSubjectsEntities().stream()
                .collect(Collectors.toMap(ExamSubjectsEntity::getExamSubjectsId, Function.identity()));

        if (studentResultDTO.getExamSubjectsDTOS() != null && !studentResultDTO.getExamSubjectsDTOS().isEmpty()) {
            for (ExamSubjectsDTO examSubjectsDTO : studentResultDTO.getExamSubjectsDTOS()) {

                ExamSubjectsEntity examSubjectsEntity;

                // UPDATE existing record
                if (examSubjectsDTO.getExamSubjectsId() != null
                        && map.containsKey(examSubjectsDTO.getExamSubjectsId())) {

                    examSubjectsEntity = map.get(examSubjectsDTO.getExamSubjectsId());

                    modelMapper.map(examSubjectsDTO, examSubjectsEntity);

                    examSubjectsEntity.setAuditDetails(addAuditDetails(examSubjectsEntity.getAuditDetails()));

                } else {

                    // CREATE new record
                    examSubjectsEntity = new ExamSubjectsEntity();

                    modelMapper.map(examSubjectsDTO, examSubjectsEntity);

                    examSubjectsEntity.setStudentResult(existingStudentResultEntity);

                    examSubjectsEntity.setAuditDetails(addAuditDetails(examSubjectsEntity.getAuditDetails()));

                    // Add to parent's collection
                    existingStudentResultEntity.getExamSubjectsEntities().add(examSubjectsEntity);
                }
            }
        }

        StudentResultEntity updatedEntity = studentResultRepository.save(existingStudentResultEntity);

        StudentResultDTO resultDTO = modelMapper.map(updatedEntity, StudentResultDTO.class);
        resultDTO.setStudentId(updatedEntity.getStudentEntity().getStudentId());

        List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
        if (updatedEntity.getExamSubjectsEntities() != null && !updatedEntity.getExamSubjectsEntities().isEmpty()) {
            for (ExamSubjectsEntity examSubjectsEntity : updatedEntity.getExamSubjectsEntities()) {
                ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                examSubjectsDTO.setResultId(resultDTO.getResultId());
                examSubjectsDTOS.add(examSubjectsDTO);
            }
        }
        resultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

        log.info("Exit from updateStudentResult");
        return resultDTO;
    }

    @Override
    public String deleteStudentResult(Long resultId) {
        log.info("Enter into deleteStudentResult");

        StudentResultEntity studentResultEntity = studentResultRepository.findById(resultId)
                .orElseThrow(() -> new CustomException("Student result not found", HttpStatus.NOT_FOUND));

        studentResultRepository.delete(studentResultEntity);

        log.info("Exit from deleteStudentResult");
        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllStudentsResultByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
