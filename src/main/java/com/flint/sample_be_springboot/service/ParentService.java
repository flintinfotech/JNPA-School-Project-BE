package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.ParentDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ParentService {

    ParentDTO getParentById(Long parentId);

    ParentDTO saveParent(ParentDTO parentDTO);

    ParentDTO updateParent(ParentDTO parentDTO);

    String deleteParent(Long parentId);

    Map<String, Object> getAllParentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
