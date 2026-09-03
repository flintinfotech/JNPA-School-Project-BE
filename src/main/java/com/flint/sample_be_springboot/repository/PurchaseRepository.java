package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity,Long>, JpaSpecificationExecutor<PurchaseEntity> {


    Optional<PurchaseEntity> findByProductCodeAndProductNameAndCategory(String productCode, String productName, String category);

    Optional<PurchaseEntity> findByProductCodeAndProductNameAndCategoryAndPurchaseIdNot(String productCode, String productName, String category, Long purchaseId);

}
