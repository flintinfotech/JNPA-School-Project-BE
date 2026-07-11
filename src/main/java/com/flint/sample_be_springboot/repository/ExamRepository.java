package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.exam.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, Long>, JpaSpecificationExecutor<ExamEntity> {

    ExamEntity findByClassRoomNameAndAcademicYearNameAndMedium(String classRoomName, String academicYearName, String medium);

    Optional<ExamEntity> findByClassRoomNameAndAcademicYearNameAndMediumAndExamIdNot(
            String classRoomName,
            String academicYearName,
            String medium,
            Long examId);

}
