package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.VendorMasterDTO;
import com.flint.sample_be_springboot.entity.VendorMasterEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.SchoolExpensesRepository;
import com.flint.sample_be_springboot.repository.VendorMasterRepository;
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
public class VendorMasterServiceImpl extends BaseService implements VendorMasterService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private VendorMasterRepository vendorMasterRepository;
    @Autowired
    private SchoolExpensesRepository schoolExpensesRepository;

    @Override
    public VendorMasterDTO saveVendorMaster(VendorMasterDTO vendorMasterDTO) {

        log.info("Enter into saveVendorMaster");

        if (vendorMasterDTO == null) {
            throw new CustomException("Vendor details cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        VendorMasterEntity vendorMasterEntity = modelMapper.map(vendorMasterDTO, VendorMasterEntity.class);
        vendorMasterEntity.setAuditDetails(addAuditDetails(vendorMasterEntity.getAuditDetails()));

        VendorMasterEntity savedVendorMasterEntity = vendorMasterRepository.save(vendorMasterEntity);

        VendorMasterDTO vendorMasterDTO1 = modelMapper.map(savedVendorMasterEntity, VendorMasterDTO.class);
        log.info("Exit from saveVendorMaster");

        return vendorMasterDTO1;
    }

    @Override
    public VendorMasterDTO getVendorMaster(Long vendorMasterId) {
        log.info("Enter into getVendorMaster");
        VendorMasterEntity existingVendorMaster = vendorMasterRepository.findById(vendorMasterId)
                .orElseThrow(() -> new CustomException("Vendor details not found", HttpStatus.NOT_FOUND));

        VendorMasterDTO vendorMasterDTO = modelMapper.map(existingVendorMaster, VendorMasterDTO.class);
        log.info("Exit from getVendorMaster");

        return vendorMasterDTO;
    }

    @Override
    public VendorMasterDTO updateVendorMaster(VendorMasterDTO vendorMasterDTO) {
        log.info("Enter into updateVendorMaster");

        if (vendorMasterDTO == null) {
            throw new CustomException("Vendor details cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        VendorMasterEntity exisitingVendorMasterEntity = vendorMasterRepository.findById(vendorMasterDTO.getVendorMasterId())
                .orElseThrow(() -> new CustomException("Vendor not found", HttpStatus.NOT_FOUND));

        //update

        exisitingVendorMasterEntity.setVendorName(vendorMasterDTO.getVendorName());
        exisitingVendorMasterEntity.setMobileNumber(vendorMasterDTO.getMobileNumber());
        exisitingVendorMasterEntity.setAddress(vendorMasterDTO.getAddress());
        exisitingVendorMasterEntity.setPinCode(vendorMasterDTO.getPinCode());
        exisitingVendorMasterEntity.setPanNumber(vendorMasterDTO.getPanNumber());
        exisitingVendorMasterEntity.setBankName(vendorMasterDTO.getBankName());
        exisitingVendorMasterEntity.setAccountNumber(vendorMasterDTO.getAccountNumber());
        exisitingVendorMasterEntity.setIfscCode(vendorMasterDTO.getIfscCode());
        exisitingVendorMasterEntity.setPaymentType(vendorMasterDTO.getPaymentType());
        exisitingVendorMasterEntity.setVendorType(vendorMasterDTO.getVendorType());
        exisitingVendorMasterEntity.setRemark(vendorMasterDTO.getRemark());
        exisitingVendorMasterEntity.setAcademicYear(vendorMasterDTO.getAcademicYear());
        exisitingVendorMasterEntity.setAuditDetails(addAuditDetails(exisitingVendorMasterEntity.getAuditDetails()));

        VendorMasterEntity updatedVendorMasterEntity = vendorMasterRepository.save(exisitingVendorMasterEntity);

        VendorMasterDTO vendorMasterDTO1 = modelMapper.map(updatedVendorMasterEntity, VendorMasterDTO.class);

        log.info("Exit from updateVendorMaster");
        return vendorMasterDTO1;
    }

    @Override
    public String deleteVendorMaster(Long vendorMasterId) {
        log.info("Enter into deleteVendorMaster");

        VendorMasterEntity vendorMaster = vendorMasterRepository.findById(vendorMasterId)
                .orElseThrow(() -> new CustomException("Vendor not found", HttpStatus.NOT_FOUND));

        vendorMasterRepository.delete(vendorMaster);

        vendorMasterRepository.delete(vendorMaster);
        log.info("Exit from deleteVendorMaster");
        return "Data deleted successfully";
    }

    @Override
    public Map<String, Object> getAllVendorMasterByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllVendorMasterByFilter");

        Page<VendorMasterEntity> vendorMasterEntityPage;
        List<VendorMasterEntity> vendorMasterEntities;
        long totalElement;

        CustomQuerySpecification<VendorMasterEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            vendorMasterEntityPage = vendorMasterRepository.findAll(customQuerySpecification, pageable);
            vendorMasterEntities = vendorMasterEntityPage.getContent();
            totalElement = vendorMasterEntityPage.getTotalElements();
        } else {
            vendorMasterEntities = vendorMasterRepository.findAll(customQuerySpecification);
            totalElement = vendorMasterEntities.size();
        }

        List<VendorMasterDTO> vendorMasterDTOList = vendorMasterEntities.stream()
                .map(vendorMasterEntity -> {
                    VendorMasterDTO vendorMasterDTO = modelMapper.map(vendorMasterEntity, VendorMasterDTO.class);
                    return vendorMasterDTO;
                }).toList();


        log.info("Exit from getAllVendorMasterByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("Data", vendorMasterDTOList);
        map.put("Total Element", totalElement);
        return map;
    }
}
