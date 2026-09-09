package com.flint.sample_be_springboot.service.FormerStudentService;

import com.flint.sample_be_springboot.dto.formerStudent.FormerStudentDocumentDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FormerStudentDocumentService {

    FormerStudentDocumentDTO saveFormerStudentDocument(FormerStudentDocumentDTO formerStudentDocumentDTO);

    FormerStudentDocumentDTO getFormerStudentDocument(Long formerStudentDocumentId);

    FormerStudentDocumentDTO updateFormerStudentDocument(FormerStudentDocumentDTO formerStudentDocumentDTO);

    String deleteFormerStudentDocument(Long formerStudentId);

    Map<String, Object> getAllFormerStudentDocumentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
