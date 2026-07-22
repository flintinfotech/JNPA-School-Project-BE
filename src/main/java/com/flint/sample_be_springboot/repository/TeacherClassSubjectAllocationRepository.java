package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.TeacherClassSubjectAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassSubjectAllocationRepository extends JpaRepository<TeacherClassSubjectAllocationEntity, Long>, JpaSpecificationExecutor<TeacherClassSubjectAllocationEntity> {

    List<TeacherClassSubjectAllocationEntity>
    findByUserInformationEntity_UserInformationIdAndClassMasterEntity_ClassMasterId(Long userInformationId, Long classMasterId);

    List<TeacherClassSubjectAllocationEntity> findByUserInformationEntity_UserInformationId(Long userInformationId);

}
