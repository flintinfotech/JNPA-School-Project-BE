package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.EmployeeSalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalaryEntity, Long>, JpaSpecificationExecutor<EmployeeSalaryEntity> {


    List<EmployeeSalaryEntity> findByAcademicYearAndEmployeeDetailsEntity_EmployeeDetailsId(String academicYear, Long employeeDetailsId);

    List<EmployeeSalaryEntity> findBySalaryDateBetweenAndAcademicYearAndEmployeeDetailsEntity_EmployeeDetailsId
            (LocalDate fromDate, LocalDate toDate, String academicYear, Long employeeDetailsId);

}
