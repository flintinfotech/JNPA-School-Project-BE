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

    // TOTAL EXPENSE COUNT - ACADEMIC YEAR WISE
    @Query("""
        SELECT COUNT(e)
        FROM SchoolExpensesEntity e
        WHERE e.academicYear = :academicYear
    """)
    Long getAllExpensesCountByAcademicYear(@Param("academicYear") String academicYear);


    // PAID EXPENSE COUNT - ACADEMIC YEAR WISE    PAID + PARTIAL
    @Query("""
        SELECT COUNT(e)
        FROM SchoolExpensesEntity e
        WHERE e.academicYear = :academicYear
          AND e.status IN :statuses
    """)
    Long getTotalPaidExpensesCountByAcademicYear(@Param("academicYear") String academicYear,
                                                 @Param("statuses") List<FeePayment> statuses);


    // PAID EXPENSE TOTAL - ACADEMIC YEAR WISE  PAID + PARTIAL
    @Query("""
        SELECT COALESCE(SUM(e.total), 0)
        FROM SchoolExpensesEntity e
        WHERE e.academicYear = :academicYear
          AND e.status IN :statuses
    """)
    BigDecimal getAllPaidExpensesTotalByAcademicYear(@Param("academicYear") String academicYear,
                                                     @Param("statuses") List<FeePayment> statuses);

    // ALL EXPENSE TOTAL - ACADEMIC YEAR WISE
    @Query("""
        SELECT COALESCE(SUM(e.total), 0)
        FROM SchoolExpensesEntity e
        WHERE e.academicYear = :academicYear
    """)
    BigDecimal getAllExpensesTotalByAcademicYear(@Param("academicYear") String academicYear);
    SELECT COUNT(e)
    FROM SchoolExpensesEntity e
    WHERE e.status IN :statuses
""")
    Long getTotalPaidExpensesCount(@Param("statuses") List<FeePayment> statuses);

    List<SchoolExpensesEntity> findByPurchaseEntity_PurchaseId(Long purchaseId);
}
