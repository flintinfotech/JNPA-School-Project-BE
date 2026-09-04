package com.flint.sample_be_springboot.repository;


import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolExpensesRepository extends JpaRepository<SchoolExpensesEntity, Long>, JpaSpecificationExecutor<SchoolExpensesEntity> {



}
