package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.ParentDTO;
import com.flint.sample_be_springboot.repository.student.ParentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class ParentServiceImpl extends BaseService implements ParentService {

    @Autowired
    private ParentRepository parentRepository;

    @Override
    public ParentDTO getParentById(Long parentId) {
        return null;
    }

    @Override
    public ParentDTO saveParent(ParentDTO parentDTO) {
        return null;
    }

    @Override
    public ParentDTO updateParent(ParentDTO parentDTO) {
        return null;
    }

    @Override
    public String deleteParent(Long parentId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllParentByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
