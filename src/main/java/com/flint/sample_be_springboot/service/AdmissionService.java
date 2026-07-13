package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.admission.AdmissionDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AdmissionService {

    AdmissionDTO getAdmissionById(Long admissionId);

    AdmissionDTO saveAdmission(AdmissionDTO admissionDTO);

    AdmissionDTO updateAdmission(AdmissionDTO admissionDTO);

    String deleteAdmission(Long admissionId);

    Map<String, Object> getAllAdmissionByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
