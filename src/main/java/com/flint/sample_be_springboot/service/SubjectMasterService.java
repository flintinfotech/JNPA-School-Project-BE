package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface SubjectMasterService {

    SubjectMasterDTO getSubjectMasterById(Long subjectMasterId);

    SubjectMasterDTO saveSubjectMaster(SubjectMasterDTO subjectMasterDTO);

    SubjectMasterDTO updateSubjectMaster(SubjectMasterDTO subjectMasterDTO);

    String deleteSubjectMaster(Long subjectMasterId);

    Map<String, Object> getAllSubjectMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
