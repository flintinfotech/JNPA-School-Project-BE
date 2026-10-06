package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.VendorMasterDTO;
import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import com.flint.sample_be_springboot.entity.VendorMasterEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.SchoolExpensesRepository;
import com.flint.sample_be_springboot.repository.VendorMasterRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VendorMasterServiceImpl extends BaseService implements VendorMasterService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private SchoolExpensesRepository schoolExpensesRepository;

    @Override
    public VendorMasterDTO saveVendorMaster(VendorMasterDTO vendorMasterDTO) {

        if (vendorMasterDTO == null) {
            throw new CustomException("Vendor details cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        VendorMasterEntity vendorMasterEntity = modelMapper.map(vendorMasterDTO, VendorMasterEntity.class);
        VendorMasterEntity savedVendorMasterEntity = vendorMasterRepository.save(vendorMasterEntity);

        VendorMasterDTO vendorMasterDTO1 = modelMapper.map(savedVendorMasterEntity, VendorMasterDTO.class);

        return vendorMasterDTO1;
    }

    @Override
    public VendorMasterDTO getVendorMaster(Long vendorMasterId) {
        return null;
    }

    @Override
    public VendorMasterDTO updateVendorMaster(VendorMasterDTO vendorMasterDTO) {
        return null;
    }

    @Override
    public String deleteVendorMaster(Long vendorMasterId) {
        return "";
    }

    @Override
    public Map<String, Object> getAllVendorMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
