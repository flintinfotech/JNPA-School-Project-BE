package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Long>, JpaSpecificationExecutor<TimeTableEntity> {
}
