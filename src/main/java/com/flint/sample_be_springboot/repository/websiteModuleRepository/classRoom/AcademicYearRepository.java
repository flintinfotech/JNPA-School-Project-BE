package com.flint.sample_be_springboot.repository.websiteModuleRepository.classRoom;

import com.flint.sample_be_springboot.entity.websiteModuleEntities.classRoom.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, Long>, JpaSpecificationExecutor<AcademicYearEntity> {

    AcademicYearEntity findByAcademicYearName(String academicYearName);

    AcademicYearEntity findByAcademicYearNameAndClassRoomEntity_ClassRoomNameAndClassRoomEntity_Medium
            (String academicYearName, String classRoomName, String medium);

    //    Optional<AcademicYearEntity> findByAcademicYearNameAndAcademicYearIdNot(String academicYearName, Long academicYearId);

    Optional<AcademicYearEntity> findByAcademicYearNameAndAcademicYearIdNot(
            String academicYearName,
            Long academicYearId);

    Optional<AcademicYearEntity> findByAcademicYearNameAndClassRoomEntity_ClassRoomNameAndClassRoomEntity_MediumAndAcademicYearIdNot(
            String academicYearName,
            String classRoomName,
            String medium,
            Long academicYearId);

}
