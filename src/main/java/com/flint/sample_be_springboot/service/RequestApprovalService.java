package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.RequestApprovalDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface RequestApprovalService {

    RequestApprovalDTO getRequestApproval(Long requestApprovalId);

    RequestApprovalDTO saveRequestApproval(RequestApprovalDTO requestApprovalDTO);

    RequestApprovalDTO updateRequestApproval(RequestApprovalDTO requestApprovalDTO);

    String deleteRequestApproval(Long requestApprovalId);

    Map<String, Object> getAllRequestApprovalByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
