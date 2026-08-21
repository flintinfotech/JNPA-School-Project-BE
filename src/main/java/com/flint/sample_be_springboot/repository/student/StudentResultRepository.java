package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResultEntity, Long>, JpaSpecificationExecutor<StudentResultEntity> {

    Optional<StudentResultEntity> findByStudentEntity_StudentId(Long studentId);

}
