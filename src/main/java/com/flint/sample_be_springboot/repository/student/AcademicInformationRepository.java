package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.AcademicInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
}
