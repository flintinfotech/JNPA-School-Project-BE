package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ClassMasterService {

    ClassMasterDTO getClassMasterById(Long classMasterId);

    ClassMasterDTO saveClassMaster(ClassMasterDTO classMasterDTO);

    ClassMasterDTO updateClassMaster(ClassMasterDTO classMasterDTO);

    String deleteClassMaster(Long classMasterId);

    Map<String, Object> getAllClassMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
