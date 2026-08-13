package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByUserNameAndUserIdNot(String userName, Long userId);

    @Query("""
    SELECT u.userEntity
    FROM UserInformationEntity u
    WHERE u.joiningDate <= :currentDate
      AND (
            u.leavingDate IS NULL
            OR u.leavingDate >= :currentDate
          )
      AND u.joiningDate <= :endDate
      AND u.leavingDate IS NULL OR u.leavingDate >= :startDate
    """)
    List<UserEntity> findCurrentWorkingUsersByAcademicYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currentDate") LocalDate currentDate
    );

}
