package com.flint.sample_be_springboot.repository.FormerStudentRepository;

import com.flint.sample_be_springboot.entity.formerStudent.FormerStudentDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FormerStudentDocumentRepository extends JpaRepository<FormerStudentDocumentEntity, Long>, JpaSpecificationExecutor<FormerStudentDocumentEntity> {
}
