package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassMasterRepository extends JpaRepository<ClassMasterEntity, Long>, JpaSpecificationExecutor<ClassMasterEntity> {

    Optional<ClassMasterEntity> findByStandardAndDivisionAndMedium(String standard, String division, String medium);

    Optional<ClassMasterEntity> findByStandardAndDivisionAndMediumAndClassMasterIdNot(String standard, String division, String medium, Long classMasterId);

}
