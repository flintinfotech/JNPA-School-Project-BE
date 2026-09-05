package com.flint.sample_be_springboot.repository;


import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import com.flint.sample_be_springboot.enums.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SchoolExpensesRepository extends JpaRepository<SchoolExpensesEntity, Long>, JpaSpecificationExecutor<SchoolExpensesEntity> {
    long count();

    @Query("""
    SELECT COALESCE(SUM(e.total), 0)
    FROM SchoolExpensesEntity e
    WHERE e.status = :status
""")
    BigDecimal getAllPaidExpensesTotal(@Param("status") FeePayment status);

    @Query("""
    SELECT COALESCE(SUM(e.total), 0)
    FROM SchoolExpensesEntity e
""")
    BigDecimal getAllExpensesTotal();

    @Query("""
    SELECT COUNT(e)
    FROM SchoolExpensesEntity e
""")
    Long getAllExpensesCount();

    @Query("""
    SELECT COUNT(e)
    FROM SchoolExpensesEntity e
    WHERE e.status IN :statuses
""")
    Long getTotalPaidExpensesCount(@Param("statuses") List<FeePayment> statuses);
}
