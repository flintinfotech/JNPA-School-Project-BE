package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDTO;

import org.springframework.data.domain.Pageable;import java.util.Map;

public interface FormerStudentService {

    FormerStudentDTO saveFormerStudent(FormerStudentDTO formerStudentDTO);

    FormerStudentDTO getFormerStudent(Long formerStudentId);

    FormerStudentDTO updateFormerStudent(FormerStudentDTO formerStudentDTO);

    String deleteFormerStudent(Long formerStudentId);

    Map<String, Object> getAllFormerStudentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
