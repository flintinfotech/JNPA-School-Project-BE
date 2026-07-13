package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.admission.AdmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionEntity, Long>, JpaSpecificationExecutor<AdmissionEntity> {

    AdmissionEntity findByClassRoomNameAndAcademicYearNameAndMedium(String classRoomName, String academicYearName, String medium);

    Optional<Object> findByClassRoomNameAndAcademicYearNameAndMediumAndAdmissionIdNot
            (String classRoomName, String academicYearName, String medium, Long admissionId);
}
