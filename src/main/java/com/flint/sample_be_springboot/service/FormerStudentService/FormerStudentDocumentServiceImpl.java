package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDocumentDTO;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class FormerStudentDocumentServiceImpl extends BaseService implements FormerStudentDocumentService {

    @Override
    public FormerStudentDocumentDTO saveFormerStudentDocument(FormerStudentDocumentDTO formerStudentDocumentDTO) {
        return null;
    }

    @Override
    public FormerStudentDocumentDTO getFormerStudentDocument(Long formerStudentDocumentId) {
        return null;
    }

    @Override
    public FormerStudentDocumentDTO updateFormerStudentDocument(FormerStudentDocumentDTO formerStudentDocumentDTO) {
        return null;
    }

    @Override
    public String deleteFormerStudentDocument(Long formerStudentId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllFormerStudentDocumentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
