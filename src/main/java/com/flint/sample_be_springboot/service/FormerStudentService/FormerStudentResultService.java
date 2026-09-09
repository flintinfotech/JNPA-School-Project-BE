package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentResultDTO;

import java.awt.print.Pageable;
import java.util.Map;

public interface FormerStudentResultService {

    FormerStudentResultDTO saveFormerStudentResult(FormerStudentResultDTO formerStudentResultDTO);

    FormerStudentResultDTO getFormerStudentResult(Long formerStudentResultId);

    FormerStudentResultDTO updateFormerStudentResult(FormerStudentResultDTO formerStudentResultDTO);

    String deleteFormerStudentResult(Long formerStudentResultId);

    Map<String, Object> getAllFormerStudentResultByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
