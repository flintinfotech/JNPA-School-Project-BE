package com.flint.sample_be_springboot.repository.FormerStudentRepository;

import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FormerStudentRepository extends JpaRepository<FormerStudentEntity, Long>, JpaSpecificationExecutor<FormerStudentEntity> {
}
