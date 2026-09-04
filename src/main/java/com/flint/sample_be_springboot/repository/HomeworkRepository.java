package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.HomeworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HomeworkRepository extends JpaRepository<HomeworkEntity, Long>, JpaSpecificationExecutor<HomeworkEntity> {
}
