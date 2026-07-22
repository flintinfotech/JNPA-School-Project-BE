package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassMasterRepository extends JpaRepository<ClassMasterEntity, Long>, JpaSpecificationExecutor<ClassMasterEntity> {

    Optional<ClassMasterEntity> findByStandardAndDivisionAndMedium(String standard, String division, String medium);

    Optional<ClassMasterEntity> findByStandardAndDivisionAndMediumAndClassMasterIdNot(String standard, String division, String medium, Long classMasterId);

    @Query("""
                SELECT c
                FROM ClassMasterEntity c
                WHERE
                    LOWER(c.standard) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(c.division) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(c.medium) LIKE LOWER(CONCAT('%', :keyword, '%'))
                ORDER BY c.standard, c.division, c.medium
            """)
    List<ClassMasterEntity> searchClasses(@Param("keyword") String keyword);

}
