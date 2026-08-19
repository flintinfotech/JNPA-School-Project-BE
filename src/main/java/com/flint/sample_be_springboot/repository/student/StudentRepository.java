package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    StudentEntity findByAadhaarCard(String aadhaarCard);

    @Query(value = "SELECT TOP 1  student_code FROM STUDENT_ENTITY ORDER BY student_id DESC", nativeQuery = true)
    String findLastStudentCode();

    Optional<StudentEntity> findByUserEntity_UserId(Long userId);

}
