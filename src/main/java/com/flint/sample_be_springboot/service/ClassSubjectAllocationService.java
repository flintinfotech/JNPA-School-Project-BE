package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;

import java.util.List;

public interface ClassSubjectAllocationService {

    String updateClassSubjects(ClassSubjectAllocationDTO dto);

    List<SubjectMasterDTO> getSubjectsByClass(Long classId);

}
