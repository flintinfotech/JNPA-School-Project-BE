package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.RequestApprovalDTO;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RequestApprovalServiceImpl extends BaseService implements RequestApprovalService {


    @Override
    public RequestApprovalDTO getRequestApproval(Long requestApprovalId) {
        log.info("Enter into getRequestApproval");
        log.info("Exit from getRequestApproval");
        return null;
    }

    @Override
    public RequestApprovalDTO saveRequestApproval(RequestApprovalDTO requestApprovalDTO) {
        log.info("Enter into saveRequestApproval");
        log.info("Exit from saveRequestApproval");
        return null;
    }

    @Override
    public RequestApprovalDTO updateRequestApproval(RequestApprovalDTO requestApprovalDTO) {
        log.info("Enter into updateRequestApproval");
        log.info("Exit from updateRequestApproval");
        return null;
    }

    @Override
    public String deleteRequestApproval(Long requestApprovalId) {
        log.info("Enter into deleteRequestApproval");
        log.info("Exit from deleteRequestApproval");
        return "";
    }

    @Override
    public Map<String, Object> getAllRequestApprovalByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllRequestApprovalByFilter");
        log.info("Exit from getAllRequestApprovalByFilter");
        return Map.of();
    }
}
