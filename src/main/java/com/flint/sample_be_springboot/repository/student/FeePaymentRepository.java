package com.flint.sample_be_springboot.repository.student;

import com.flint.sample_be_springboot.entity.student.FeePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeePaymentRepository extends JpaRepository<FeePaymentEntity, Long>, JpaSpecificationExecutor<FeePaymentEntity> {

    @Query("""
        SELECT MAX(f.receiptNo)
        FROM FeePaymentEntity f
        """)
    String findLastReceiptNo();
}
