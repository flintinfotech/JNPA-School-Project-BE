package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentAchievementsDTO;
import com.flint.sample_be_springboot.entity.student.StudentAchievementsEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.student.StudentAchievementsRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class StudentAchievementsServiceImpl extends BaseService implements StudentAchievementsService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private StudentAchievementsRepository studentAchievementsRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentAchievementsDTO getStudentAchievementsById(Long studentAchievementId) {

        log.info("Enter into getStudentAchievementsById");
        StudentAchievementsDTO achievementsDTO;

        StudentAchievementsEntity existingStudentAchievementsEntity = studentAchievementsRepository.findById(studentAchievementId)
                .orElseThrow(() -> new CustomException(" Student achievement not exist", HttpStatus.PRECONDITION_FAILED));

        achievementsDTO = modelMapper.map(existingStudentAchievementsEntity, StudentAchievementsDTO.class);

        log.info("Exit from getStudentAchievementsById");
        return achievementsDTO;
    }

    @Override

    public StudentAchievementsDTO saveStudentAchievements(StudentAchievementsDTO studentAchievementsDTO) {

        log.info("Enter into saveStudentAchievements");

        if (studentAchievementsDTO == null) {
            throw new CustomException("Student achievement info can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (studentAchievementsDTO.getStudentId() == null || studentAchievementsDTO.getStudentId() == null) {
            throw new CustomException("Student ID can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentEntity existingStudentEntity = studentRepository.findById(studentAchievementsDTO.getStudentId())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentAchievementsEntity achievementsEntity = modelMapper.map(studentAchievementsDTO, StudentAchievementsEntity.class);
        achievementsEntity.setStudentEntity(existingStudentEntity);

        StudentAchievementsEntity savedAchievement = studentAchievementsRepository.save(achievementsEntity);

        StudentAchievementsDTO achievementsDTO = modelMapper.map(savedAchievement, StudentAchievementsDTO.class);
        log.info("Exit from saveStudentAchievements");

        return achievementsDTO;
    }

    @Override
    public StudentAchievementsDTO updateStudentAchievements(StudentAchievementsDTO studentAchievementsDTO) {

        log.info("Enter into updateStudentAchievements");

        log.info("Exit from updateStudentAchievements");
        return null;
    }


    @Override
    public String deleteStudentAchievements(Long studentAchievementId) {

        StudentAchievementsEntity existingStudentAchievement = studentAchievementsRepository.findById(studentAchievementId)
                .orElseThrow(() ->
                        new CustomException("User information not found", HttpStatus.NOT_FOUND));

        studentAchievementsRepository.delete(existingStudentAchievement);

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllStudentAchievementsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
