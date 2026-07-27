package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ClassSubjectAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassSubjectAllocationRepository extends JpaRepository<ClassSubjectAllocationEntity, Long>, JpaSpecificationExecutor<ClassSubjectAllocationEntity> {

    @Query("""
            SELECT c
            FROM ClassSubjectAllocationEntity c
            WHERE c.classMasterEntity.classMasterId = :classMasterId
              AND c.startDate <= :endDate
              AND c.endDate >= :startDate
            """)
    List<ClassSubjectAllocationEntity> findByClassMasterIdAndAcademicYear(
            @Param("classMasterId") Long classMasterId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
            FROM ClassSubjectAllocationEntity c
            WHERE c.classMasterEntity.classMasterId = :classMasterId
              AND c.subjectMasterEntity.subjectMasterId = :subjectMasterId
              AND c.startDate <= :endDate
              AND c.endDate >= :startDate
            """)
    boolean existsByClassMasterIdAndSubjectMasterIdAndAcademicYear(
            @Param("classMasterId") Long classMasterId,
            @Param("subjectMasterId") Long subjectMasterId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
