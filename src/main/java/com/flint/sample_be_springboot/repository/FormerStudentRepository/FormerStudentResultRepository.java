package com.flint.sample_be_springboot.repository.FormerStudentRepository;

import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FormerStudentResultRepository extends JpaRepository<FormerStudentResultEntity, Long>, JpaSpecificationExecutor<FormerStudentResultEntity> {
}
