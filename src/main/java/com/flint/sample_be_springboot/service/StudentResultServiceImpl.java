package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.ExamSubjectsDTO;
import com.flint.sample_be_springboot.dto.student.StudentResultDTO;
import com.flint.sample_be_springboot.entity.student.ExamSubjectsEntity;
import com.flint.sample_be_springboot.entity.student.StudentResultEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.student.ExamSubjectsRepository;
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

@Slf4j
@Service
public class StudentResultServiceImpl extends BaseService implements StudentResultService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private StudentResultRepository studentResultRepository;
    @Autowired
    private ExamSubjectsRepository examSubjectsRepository;

    @Override
    public StudentResultDTO saveStudentResult(StudentResultDTO studentResultDTO) {
        log.info("Enter into saveStudentResult");

        if (studentResultDTO == null) {
            throw new CustomException("Student result info can't be null", HttpStatus.PRECONDITION_FAILED);
        }


        StudentResultEntity studentResultEntity = modelMapper.map(studentResultDTO, StudentResultEntity.class);
        studentResultEntity.setAuditDetails(addAuditDetails(studentResultEntity.getAuditDetails()));

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

        List<ExamSubjectsDTO> examSubjectsDTOS = new ArrayList<>();
        if (savedResult.getExamSubjectsEntities() != null && !savedResult.getExamSubjectsEntities().isEmpty()) {
            for (ExamSubjectsEntity examSubjectsEntity : savedResult.getExamSubjectsEntities()) {
                ExamSubjectsDTO examSubjectsDTO = modelMapper.map(examSubjectsEntity, ExamSubjectsDTO.class);
                examSubjectsDTO.setResultId(resultDTO.getResultId());
                examSubjectsDTOS.add(examSubjectsDTO);
            }
        }
        resultDTO.setExamSubjectsDTOS(examSubjectsDTOS);

        return resultDTO;

    }


    @Override
    public StudentResultDTO getStudentResultById(Long resultId) {
        return null;
    }


    @Override
    public StudentResultDTO updateStudentResult(StudentResultDTO studentResultDTO) {
        return null;
    }

    @Override
    public String deleteStudentResult(Long resultId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllStudentResultByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
