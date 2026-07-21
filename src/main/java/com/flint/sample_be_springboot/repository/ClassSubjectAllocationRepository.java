package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSubjectAllocationRepository extends JpaRepository<ClassSubjectAllocationEntity, Long>, JpaSpecificationExecutor<ClassSubjectAllocationEntity> {

    List<ClassSubjectAllocationEntity> findByClassMasterEntity_ClassMasterId(Long classMasterId);

    Optional<ClassSubjectAllocationEntity> findByClassMasterEntity_ClassMasterIdAndSubjectMasterEntity_SubjectMasterId(Long classMasterId, Long subjectMasterId);

}
