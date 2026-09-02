package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.AcademicInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicInformationRepository extends JpaRepository<AcademicInformationEntity, Long>, JpaSpecificationExecutor<AcademicInformationEntity> {

    @Query(value = """
    SELECT TOP 1 ADMISSION_NO
    FROM ACADEMIC_INFORMATION_ENTITY
    WHERE ADMISSION_NO IS NOT NULL
    ORDER BY ACADEMIC_INFORMATION_ID DESC
    """, nativeQuery = true)
    String findLastAdmissionNo();

    @Query("""
    SELECT MAX(a.rollNo)
    FROM AcademicInformationEntity a
    WHERE a.standard = :standard
      AND a.division = :division
      AND a.medium = :medium
      AND a.academicYear = :academicYear
""")
    Integer findMaxRollNoByStandardAndDivisionAndMediumAndAcademicYear(
            @Param("standard") String standard,
            @Param("division") String division,
            @Param("medium") String medium,
            @Param("academicYear") String academicYear
    );
}
