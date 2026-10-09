package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.TimeTablePeriodEntity;
import com.flint.sample_be_springboot.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeTablePeriodRepository extends JpaRepository<TimeTablePeriodEntity, Long>, JpaSpecificationExecutor<TimeTablePeriodEntity> {

    List<TimeTablePeriodEntity> findByEmployeeDetailsEntity_EmployeeDetailsId(Long employeeDetailsId);


    @Query(value = """
    SELECT COUNT(*)
    FROM TIME_TABLE_PERIOD tp
    INNER JOIN TIME_TABLE tt
        ON tt.TIME_TABLE_ID = tp.TIME_TABLE_ID
    WHERE tt.ACADEMIC_YEAR = :academicYear
      AND tp.TEACHER_ID = :employeeDetailsId
      AND tp.DAY = :day
      AND tp.START_TIME < CAST(:endTime AS time)
      AND tp.END_TIME > CAST(:startTime AS time)
    """, nativeQuery = true)
    long countTeacherTimeConflict(
            @Param("academicYear") String academicYear,
            @Param("employeeDetailsId") Long employeeDetailsId,
            @Param("day") String day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query(value = """
    SELECT COUNT(*)
    FROM TIME_TABLE_PERIOD tp
    INNER JOIN TIME_TABLE tt
        ON tt.TIME_TABLE_ID = tp.TIME_TABLE_ID
    WHERE tt.ACADEMIC_YEAR = :academicYear
      AND tp.TEACHER_ID = :employeeDetailsId
      AND tp.DAY = :day
      AND tp.START_TIME < CAST(:endTime AS TIME)
      AND tp.END_TIME > CAST(:startTime AS TIME)
      AND tp.TIME_TABLE_PERIOD_ID <> :timeTablePeriodId
    """, nativeQuery = true)
    long countTeacherTimeConflictForUpdate(
            @Param("academicYear") String academicYear,
            @Param("employeeDetailsId") Long employeeDetailsId,
            @Param("day") String day,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("timeTablePeriodId") Long timeTablePeriodId
    );
    }


