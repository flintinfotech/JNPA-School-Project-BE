package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, Long>, JpaSpecificationExecutor<AcademicYearEntity> {

    AcademicYearEntity findByAcademicYearName(String academicYearName);

//    Optional<AcademicYearEntity> findByAcademicYearNameAndAcademicYearIdNot(String academicYearName, Long academicYearId);

    Optional<AcademicYearEntity> findByAcademicYearNameAndAcademicYearIdNot(
            String academicYearName,
            Long academicYearId);

}
