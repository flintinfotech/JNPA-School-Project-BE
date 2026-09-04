package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import com.flint.sample_be_springboot.entity.PurchaseEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.PurchaseRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseServiceImpl extends BaseService implements PurchaseService {


    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public PurchaseDTO savePurchase(PurchaseDTO purchaseDTO) {

        log.info("Enter into savePurchase");

        if (purchaseDTO == null) {
            throw new CustomException("Purchase information can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        Optional<PurchaseEntity> existingPurchaseEntity = purchaseRepository.findByProductCodeAndProductNameAndCategory
                (purchaseDTO.getProductCode(), purchaseDTO.getProductName(), purchaseDTO.getCategory());
        if (existingPurchaseEntity.isPresent()) {
            throw new CustomException("product is already exist", HttpStatus.CONFLICT);
        }

        PurchaseEntity purchaseEntity = modelMapper.map(purchaseDTO, PurchaseEntity.class);
        //save
        PurchaseEntity savedPurchaseEntity = purchaseRepository.save(purchaseEntity);

        //return DTO
        PurchaseDTO dto = modelMapper.map(savedPurchaseEntity, PurchaseDTO.class);
        log.info("Exit from savePurchase");

        return dto;
    }

    @Override
    public PurchaseDTO getPurchaseByPurchaseId(Long purchaseId) {

        log.info("Enter into getPurchaseByPurchaseId");

        if (purchaseId == null) {
            throw new CustomException("Purchase ID can not be null", HttpStatus.BAD_REQUEST);
        }

        PurchaseEntity purchaseEntity = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new CustomException("Data not found", HttpStatus.NOT_FOUND));

        PurchaseDTO dto = modelMapper.map(purchaseEntity, PurchaseDTO.class);

        log.info("Exit from getPurchaseByPurchaseId");
        return dto;
    }

    @Override
    public PurchaseDTO updatePurchase(PurchaseDTO purchaseDTO) {

        log.info("Enter into updatePurchase");

        if (purchaseDTO == null) {
            throw new CustomException("Purchase information can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        PurchaseEntity existingPurchaseEntity = purchaseRepository.findById(purchaseDTO.getPurchaseId())
                .orElseThrow(() -> new CustomException("data not exist", HttpStatus.NOT_FOUND));

        Optional<PurchaseEntity> existingPurchaseEntity1 = purchaseRepository.findByProductCodeAndProductNameAndCategoryAndPurchaseIdNot
                (purchaseDTO.getProductCode(), purchaseDTO.getProductName(), purchaseDTO.getCategory(), purchaseDTO.getPurchaseId());
        if (existingPurchaseEntity1.isPresent()) {
            throw new CustomException("product already exist", HttpStatus.CONFLICT);
        }

        //update
        existingPurchaseEntity.setCategory(purchaseDTO.getCategory());
        existingPurchaseEntity.setProductCode(purchaseDTO.getProductCode());
        existingPurchaseEntity.setProductName(purchaseDTO.getProductName());

        PurchaseEntity updatedPurchaseEntity = purchaseRepository.save(existingPurchaseEntity);

        //Convert into DTO
        PurchaseDTO dto = modelMapper.map(updatedPurchaseEntity, PurchaseDTO.class);
        log.info("Exit from updatePurchase");

        return dto;
    }

    @Override
    public String deletePurchase(Long purchaseId) {

        log.info("Enter into deletePurchase");

        if (purchaseId == null) {
            throw new CustomException("Purchase ID can not be null", HttpStatus.BAD_REQUEST);
        }
        PurchaseEntity purchaseEntity = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new CustomException("Purchase not found", HttpStatus.NOT_FOUND));

        purchaseRepository.delete(purchaseEntity);
        log.info("Exit from deletePurchase");
        return "Data deleted successfully";
    }

    @Override
    public Map<String, Object> getAllPurchaseByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {

        Page<PurchaseEntity> purchaseEntityPage;
        List<PurchaseEntity> purchaseEntities;
        long totalElement;

        CustomQuerySpecification<PurchaseEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            purchaseEntityPage = purchaseRepository.findAll(customQuerySpecification, pageable);
            purchaseEntities = purchaseEntityPage.getContent();
            totalElement = purchaseEntityPage.getTotalElements();
        } else {
            purchaseEntities = purchaseRepository.findAll(customQuerySpecification);
            totalElement = purchaseEntities.size();
        }

        List<PurchaseDTO> purchaseDTOS = purchaseEntities.stream()
                .map(p -> modelMapper.map(p, PurchaseDTO.class))
                .collect(Collectors.toList());


        Map<String, Object> map = new HashMap<>();
        map.put("PurchaseDTOS", purchaseDTOS);
        map.put("Total Elements", totalElement);
        return map;
    }
}
