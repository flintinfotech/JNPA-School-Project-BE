package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetailsEntity, Long>, JpaSpecificationExecutor<EmployeeDetailsEntity> {

    Optional<EmployeeDetailsEntity> findByUserEntity_UserId(Long userId);

    @Query("""
        SELECT u
        FROM EmployeeDetailsEntity u
        WHERE u.joiningDate <= :startDate
          AND (
                u.leavingDate IS NULL
                OR u.leavingDate >= :endDate
              )
        """)
    List<EmployeeDetailsEntity> findCurrentWorkingUsersByAcademicYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<EmployeeDetailsEntity> findByUserName(String userName);
}
