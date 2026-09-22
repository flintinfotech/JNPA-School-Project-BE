package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendanceEntity, Long>, JpaSpecificationExecutor<StudentAttendanceEntity> {

    StudentAttendanceEntity findByAttendanceDateAndAcademicYearAndStudentEntity_StudentId(LocalDate attendanceDate, Long studentId, String academicYear);

}
