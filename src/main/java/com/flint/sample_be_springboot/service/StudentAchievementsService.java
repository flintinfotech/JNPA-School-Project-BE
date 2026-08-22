package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentAchievementsDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudentAchievementsService {

    StudentAchievementsDTO saveStudentAchievements(StudentAchievementsDTO studentAchievementsDTO);

    StudentAchievementsDTO getStudentAchievementsById(Long studentAchievementId);

    StudentAchievementsDTO updateStudentAchievements(StudentAchievementsDTO studentAchievementsDTO);

    String deleteStudentAchievements(Long studentAchievementId);

    Map<String, Object> getAllStudentAchievementsByFilter(Map<String, Object> filter,Pageable pageable,boolean paginate);
}
