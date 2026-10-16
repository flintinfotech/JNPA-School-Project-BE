package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubjectMasterRepository extends JpaRepository<SubjectMasterEntity, Long>, JpaSpecificationExecutor<SubjectMasterEntity> {

    Optional<SubjectMasterEntity> findBySubjectNameAndSectionAndMedium(String subjectName,String section,String medium);

    Optional<SubjectMasterEntity> findBySubjectNameAndSectionAndMediumAndSubjectMasterIdNot(String subjectName,String section,String medium, Long subjectId);

}
