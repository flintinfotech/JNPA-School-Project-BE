package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, Long>, JpaSpecificationExecutor<ParentEntity> {
}
