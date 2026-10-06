package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import com.flint.sample_be_springboot.dto.RequestApprovalDTO;
import com.flint.sample_be_springboot.entity.PurchaseEntity;
import com.flint.sample_be_springboot.entity.RequestApprovalEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.PurchaseRepository;
import com.flint.sample_be_springboot.repository.RequestApprovalRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RequestApprovalServiceImpl extends BaseService implements RequestApprovalService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private RequestApprovalRepository requestApprovalRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public RequestApprovalDTO saveRequestApproval(RequestApprovalDTO requestApprovalDTO) {

        log.info("Enter into saveRequestApproval");

        if (requestApprovalDTO == null) {
            throw new CustomException("Request approval info cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        RequestApprovalEntity requestApprovalEntity = modelMapper.map(requestApprovalDTO, RequestApprovalEntity.class);

        if (requestApprovalDTO.getPurchaseDTO() != null && requestApprovalDTO.getPurchaseDTO().getPurchaseId() != null) {

            PurchaseEntity purchaseEntity = purchaseRepository.findById(requestApprovalDTO.getPurchaseDTO().getPurchaseId())
                    .orElseThrow(() -> new CustomException("Purchase not found", HttpStatus.NOT_FOUND));

            requestApprovalEntity.setPurchaseEntity(purchaseEntity);
        }

        RequestApprovalEntity savedEntity = requestApprovalRepository.save(requestApprovalEntity);

        RequestApprovalDTO approvalDTO = modelMapper.map(savedEntity, RequestApprovalDTO.class);
        if (savedEntity.getPurchaseEntity() != null) {

            PurchaseDTO purchaseDTO = modelMapper.map(savedEntity.getPurchaseEntity(), PurchaseDTO.class);
            approvalDTO.setPurchaseDTO(purchaseDTO);
        }

        log.info("Exit from saveRequestApproval");

        return approvalDTO;
    }


    @Override
    public RequestApprovalDTO getRequestApproval(Long requestApprovalId) {
        log.info("Enter into getRequestApproval");

        RequestApprovalEntity requestApprovalEntity = requestApprovalRepository.findById(requestApprovalId)
                .orElseThrow(() -> new CustomException("Request not found", HttpStatus.PRECONDITION_FAILED));

        RequestApprovalDTO requestApprovalDTO = modelMapper.map(requestApprovalEntity, RequestApprovalDTO.class);

        if (requestApprovalEntity.getPurchaseEntity() != null) {
            PurchaseDTO purchaseDTO = modelMapper.map(requestApprovalEntity.getPurchaseEntity(), PurchaseDTO.class);
            requestApprovalDTO.setPurchaseDTO(purchaseDTO);
        }

        log.info("Exit from getRequestApproval");
        return requestApprovalDTO;
    }


    @Override
    public RequestApprovalDTO updateRequestApproval(RequestApprovalDTO requestApprovalDTO) {
        log.info("Enter into updateRequestApproval");

        if (requestApprovalDTO == null) {
            throw new CustomException("Request approval info cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        RequestApprovalEntity requestApprovalEntity = requestApprovalRepository.findById(requestApprovalDTO.getRequestApprovalId())
                .orElseThrow(() -> new CustomException("Request not found", HttpStatus.PRECONDITION_FAILED));

        //update

        requestApprovalEntity.setRequestType(requestApprovalDTO.getRequestType());
        requestApprovalEntity.setRequestedDate(requestApprovalDTO.getRequestedDate());
        requestApprovalEntity.setStatus(requestApprovalDTO.getStatus());
        requestApprovalEntity.setQuantity(requestApprovalDTO.getQuantity());
        requestApprovalEntity.setPriority(requestApprovalDTO.getPriority());
        requestApprovalEntity.setAcademicYear(requestApprovalDTO.getAcademicYear());
        requestApprovalEntity.setEstimatedAmount(requestApprovalDTO.getEstimatedAmount());
        requestApprovalEntity.setRemark(requestApprovalDTO.getRemark());

        if (requestApprovalDTO.getPurchaseDTO() != null) {
            if (requestApprovalEntity.getPurchaseEntity() == null) {
                throw new CustomException("Purchase information not found for this request", HttpStatus.PRECONDITION_FAILED);
            }

            PurchaseEntity purchaseEntity = requestApprovalEntity.getPurchaseEntity();

            purchaseEntity.setProductName(requestApprovalDTO.getPurchaseDTO().getProductName());
            purchaseEntity.setProductCode(requestApprovalDTO.getPurchaseDTO().getProductCode());
            purchaseEntity.setPurchaseDate(requestApprovalDTO.getPurchaseDTO().getPurchaseDate());
            purchaseEntity.setCategory(requestApprovalDTO.getPurchaseDTO().getCategory());

            purchaseRepository.save(purchaseEntity);
        }

        RequestApprovalEntity savedRequestApprovalEntity = requestApprovalRepository.save(requestApprovalEntity);

        RequestApprovalDTO requestApprovalDTO1 = modelMapper.map(savedRequestApprovalEntity, RequestApprovalDTO.class);

        if (savedRequestApprovalEntity.getPurchaseEntity() != null) {

            PurchaseDTO purchaseDTO = modelMapper.map(savedRequestApprovalEntity.getPurchaseEntity(), PurchaseDTO.class);
            requestApprovalDTO1.setPurchaseDTO(purchaseDTO);
        }


        log.info("Exit from updateRequestApproval");
        return requestApprovalDTO1;
    }

    @Override
    public String deleteRequestApproval(Long requestApprovalId) {
        log.info("Enter into deleteRequestApproval");

        if (requestApprovalId == null) {
            throw new CustomException("Request Id cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (!requestApprovalRepository.existsById(requestApprovalId)) {
            throw new CustomException("Request not found", HttpStatus.NOT_FOUND);
        }

        requestApprovalRepository.deleteById(requestApprovalId);
        log.info("Exit from deleteRequestApproval");
        return "Request approval deleted successfully";

    }

    @Override
    public Map<String, Object> getAllRequestApprovalByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllRequestApprovalByFilter");

        Page<RequestApprovalEntity> requestApprovalEntityPage;
        List<RequestApprovalEntity> requestApprovalEntities;
        long totalElement;

        CustomQuerySpecification<RequestApprovalEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            requestApprovalEntityPage = requestApprovalRepository.findAll(customQuerySpecification, pageable);
            requestApprovalEntities = requestApprovalEntityPage.getContent();
            totalElement = requestApprovalEntityPage.getTotalElements();
        } else {
            requestApprovalEntities = requestApprovalRepository.findAll(customQuerySpecification);
            totalElement = requestApprovalEntities.size();
        }

        List<RequestApprovalDTO> requestApprovalDTOList = requestApprovalEntities.stream()
                .map(requestApprovalEntity -> {

                    RequestApprovalDTO requestApprovalDTO = modelMapper.map(requestApprovalEntity, RequestApprovalDTO.class);

                    // Map PurchaseEntity -> PurchaseDTO
                    if (requestApprovalEntity.getPurchaseEntity() != null) {
                        PurchaseDTO purchaseDTO = modelMapper.map(requestApprovalEntity.getPurchaseEntity(), PurchaseDTO.class);
                        requestApprovalDTO.setPurchaseDTO(purchaseDTO);
                    }

                    return requestApprovalDTO;
                })
                .toList();


        log.info("Exit from getAllRequestApprovalByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("Data", requestApprovalDTOList);
        map.put("Total Elements", totalElement);

        return map;
    }
}
