package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResultEntity, Long>, JpaSpecificationExecutor<StudentResultEntity> {

}
