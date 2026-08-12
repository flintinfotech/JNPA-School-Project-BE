package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.StudentFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFeeRepository extends JpaRepository<StudentFeeEntity, Long>, JpaSpecificationExecutor<StudentFeeEntity> {

}
