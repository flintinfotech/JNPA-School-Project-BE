package com.flint.sample_be_springboot.dto;

import lombok.Data;

@Data
public class PurchaseDTO {

    private Long purchaseId;
    private  String productCode;
    private String category;
    private String productName;

}
