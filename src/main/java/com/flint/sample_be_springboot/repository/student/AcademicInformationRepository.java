package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.AcademicInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicInformationRepository extends JpaRepository<AcademicInformationEntity, Long>, JpaSpecificationExecutor<AcademicInformationEntity> {
}
