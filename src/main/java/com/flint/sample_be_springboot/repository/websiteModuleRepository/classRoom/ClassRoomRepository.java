package com.flint.sample_be_springboot.repository.websiteModuleRepository.classRoom;

import com.flint.sample_be_springboot.entity.websiteModuleEntities.classRoom.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Long>, JpaSpecificationExecutor<ClassRoomEntity> {

    ClassRoomEntity findByClassRoomNameAndAcademicYearNameAndMedium(String classRoomName, String academicYearName, String medium);

    Optional<ClassRoomEntity> findByClassRoomNameAndAcademicYearNameAndMediumAndClassRoomIdNot(
            String classRoomName,
            String academicYearName,
            String medium,
            Long classRoomId);

}
