package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentAchievementsDTO;

public interface StudentAchievementsService {

    StudentAchievementsDTO saveStudentAchievements(StudentAchievementsDTO studentAchievementsDTO);

    StudentAchievementsDTO getStudentAchievementsById(Long studentAchievementId);

    StudentAchievementsDTO updateStudentAchievements(StudentAchievementsDTO studentAchievementsDTO);

    String deleteStudentAchievements(Long studentAchievementId);

}
