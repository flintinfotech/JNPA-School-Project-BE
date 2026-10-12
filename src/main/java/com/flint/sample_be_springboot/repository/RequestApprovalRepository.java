package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.RequestApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApprovalEntity, Long>, JpaSpecificationExecutor<RequestApprovalEntity> {

    @Query(value = "SELECT TOP 1 ORDER_NUMBER " +
            "FROM REQUEST_APPROVAL_ENTITY " +
            "WHERE ORDER_NUMBER IS NOT NULL " +
            "AND LTRIM(RTRIM(ORDER_NUMBER)) <> '' " +
            "ORDER BY REQUEST_APPROVAL_ID DESC",
            nativeQuery = true)
    String findLastOrderNumber();
}
