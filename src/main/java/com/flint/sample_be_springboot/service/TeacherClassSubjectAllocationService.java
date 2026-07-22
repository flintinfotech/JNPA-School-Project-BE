package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.ClassMasterDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.dto.TeacherClassSubjectAllocationDTO;
import com.flint.sample_be_springboot.entity.ClassMasterEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;

import java.util.List;
import java.util.Map;

public interface TeacherClassSubjectAllocationService {

    String updateTeacherClassSubjectAllocation(TeacherClassSubjectAllocationDTO dto);

    Map<ClassMasterDTO, List<SubjectMasterDTO>> getTeacherClassSubjectAllocation(Long userInformationId);

}
