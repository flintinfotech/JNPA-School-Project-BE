package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.AdmissionInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionInquiryRepository extends JpaRepository<AdmissionInquiry, Long>, JpaSpecificationExecutor<AdmissionInquiry> {

}
