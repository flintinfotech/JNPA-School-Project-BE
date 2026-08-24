package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.AcademicCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendarEntity, Long>, JpaSpecificationExecutor<AcademicCalendarEntity> {

    Optional<AcademicCalendarEntity> findByStartDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<AcademicCalendarEntity> findByStartDateBetweenAndAcademicCalendarIdNot(
            LocalDate startDate,
            LocalDate endDate,
            Long academicCalendarId
    );
}
