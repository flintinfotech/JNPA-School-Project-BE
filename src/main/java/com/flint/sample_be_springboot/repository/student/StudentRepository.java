package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    StudentEntity findByAadhaarCard(String aadhaarCard);

    @Query(value = "SELECT TOP 1  student_code FROM STUDENT_ENTITY ORDER BY student_id DESC", nativeQuery = true)
    String findLastStudentCode();


    Optional<StudentEntity> findByUserEntity_UserId(Long userId);

    @Query("""
            SELECT DISTINCT s
            FROM StudentEntity s
            JOIN s.academicInformationEntity a
            WHERE a.academicYear = :academicYear
              AND (:standard IS NULL OR :standard = '' OR a.standard = :standard)
              AND (:division IS NULL OR :division = '' OR a.division = :division)
              AND (:medium IS NULL OR :medium = '' OR a.medium = :medium)
            """)
    Page<StudentEntity> findAllCurrentYearStudents(
            @Param("academicYear") String academicYear,
            @Param("standard") String standard,
            @Param("division") String division,
            @Param("medium") String medium,
            Pageable pageable);

    @Query("""
        SELECT DISTINCT s
        FROM StudentEntity s
        JOIN s.academicInformationEntity a
        WHERE a.academicYear = :academicYear
          AND (:standard IS NULL OR :standard = '' OR a.standard = :standard)
          AND (:division IS NULL OR :division = '' OR a.division = :division)
          AND (:medium IS NULL OR :medium = '' OR a.medium = :medium)
        """)
    List<StudentEntity> findAllCurrentYearStudents(
            @Param("academicYear") String academicYear,
            @Param("standard") String standard,
            @Param("division") String division,
            @Param("medium") String medium);

}
