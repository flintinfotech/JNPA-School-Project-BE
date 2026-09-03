package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PurchaseService {

    PurchaseDTO savePurchase(PurchaseDTO purchaseDTO);

    PurchaseDTO getPurchaseByPurchaseId(Long purchaseId);

    PurchaseDTO updatePurchase(PurchaseDTO purchaseDTO );

    String deletePurchase(Long purchaseId);

    Map<String, Object> getAllPurchaseByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
