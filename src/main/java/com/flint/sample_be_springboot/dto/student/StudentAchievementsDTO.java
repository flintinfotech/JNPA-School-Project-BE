package com.flint.sample_be_springboot.dto.student;

import lombok.Data;

@Data
public class StudentAchievementsDTO {

    private Long studentAchievementId;
    private String achievementName;
    private String achievementDescription;
    private String academicYear;
    private Long studentId;

}
