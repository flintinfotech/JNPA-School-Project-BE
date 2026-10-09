package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.VendorMasterDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface VendorMasterService {

    VendorMasterDTO saveVendorMaster(VendorMasterDTO vendorMasterDTO);

    VendorMasterDTO getVendorMaster(Long vendorMasterId);

    VendorMasterDTO updateVendorMaster(VendorMasterDTO vendorMasterDTO);

    String deleteVendorMaster(Long vendorMasterId);

    Map<String, Object> getAllVendorMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
