package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import com.flint.sample_be_springboot.entity.TeacherClassSubjectAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TeacherClassSubjectAllocationRepository extends JpaRepository<TeacherClassSubjectAllocationEntity, Long>, JpaSpecificationExecutor<TeacherClassSubjectAllocationEntity> {

    @Query("""
            SELECT t
            FROM TeacherClassSubjectAllocationEntity t
            WHERE t.employeeDetailsEntity.employeeDetailsId = :userId
              AND t.classMasterEntity.classMasterId = :classId
              AND t.startDate <= :endDate
              AND t.endDate >= :startDate
            """)
    List<TeacherClassSubjectAllocationEntity> findByEmployeeDetailsIdAndClassMasterIdAndAcademicYear(
            @Param("userId") Long userId,
            @Param("classId") Long classId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT t
            FROM TeacherClassSubjectAllocationEntity t
            WHERE t.employeeDetailsEntity.employeeDetailsId = :employeeDetailsId
              AND t.startDate <= :endDate
              AND t.endDate >= :startDate
            """)
    List<TeacherClassSubjectAllocationEntity> findByEmployeeDetailsIdAndAcademicYear(
            @Param("employeeDetailsId") Long employeeDetailsId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<TeacherClassSubjectAllocationEntity> findBySubjectMasterEntity_SubjectMasterId(Long subjectMasterId);

    List<TeacherClassSubjectAllocationEntity> findByClassMasterEntity_ClassMasterId(Long classMasterId);

}
