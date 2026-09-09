package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentResultDTO;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Map;

@Service
@Slf4j
public class FormerStudentResultServiceImpl extends BaseService implements FormerStudentResultService {


    @Override
    public FormerStudentResultDTO saveFormerStudentResult(FormerStudentResultDTO formerStudentResultDTO) {
        return null;
    }

    @Override
    public FormerStudentResultDTO getFormerStudentResult(Long formerStudentResultId) {
        return null;
    }

    @Override
    public FormerStudentResultDTO updateFormerStudentResult(FormerStudentResultDTO formerStudentResultDTO) {
        return null;
    }

    @Override
    public String deleteFormerStudentResult(Long formerStudentResultId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllFormerStudentResultByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
