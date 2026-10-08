package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.RequestApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApprovalEntity,Long>, JpaSpecificationExecutor<RequestApprovalEntity> {

    @Query(value = "SELECT TOP 1  order_number FROM REQUEST_APPROVAL_ENTITY ORDER BY REQUEST_APPROVAL_ID DESC", nativeQuery = true)
    String findLastOrderNumber();
}
