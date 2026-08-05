package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.AdmissionInquiryDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AdmissionInquiryService {


    AdmissionInquiryDTO saveAdmissionInquiry(AdmissionInquiryDTO admissionInquiryDTO);

    AdmissionInquiryDTO updateAdmissionInquiryById(Long id, AdmissionInquiryDTO admissionInquiryDTO);

    AdmissionInquiryDTO deleteAdmissionInquiryById(Long id);

    Map<String, Object> getAllAdmissionInquiryByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);


}
