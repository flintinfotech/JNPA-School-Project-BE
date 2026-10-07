package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import com.flint.sample_be_springboot.dto.SchoolExpensesDTO;
import com.flint.sample_be_springboot.dto.SchoolExpensesReportDTO;
import com.flint.sample_be_springboot.dto.VendorMasterDTO;
import com.flint.sample_be_springboot.entity.PurchaseEntity;
import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import com.flint.sample_be_springboot.entity.VendorMasterEntity;
import com.flint.sample_be_springboot.enums.FeePayment;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.PurchaseRepository;
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

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchoolExpensesServiceImpl extends BaseService implements SchoolExpensesService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private SchoolExpensesRepository schoolExpensesRepository;

    @Autowired
    private VendorMasterRepository vendorMasterRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public SchoolExpensesDTO saveSchoolExpenses(SchoolExpensesDTO schoolExpensesDTO) {
        log.info("Enter into saveSchoolExpenses");

        if (schoolExpensesDTO == null) {
            throw new CustomException("Expenses information can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        SchoolExpensesEntity schoolExpensesEntity = modelMapper.map(schoolExpensesDTO, SchoolExpensesEntity.class);

        // Calculate pending amount automatically
        BigDecimal total = schoolExpensesDTO.getTotal() != null
                ? schoolExpensesDTO.getTotal()
                : BigDecimal.ZERO;

        BigDecimal paidAmount = schoolExpensesDTO.getPaidAmount() != null
                ? schoolExpensesDTO.getPaidAmount()
                : BigDecimal.ZERO;

        BigDecimal pendingAmount = total.subtract(paidAmount);

        if (paidAmount.compareTo(total) == 0) {
            schoolExpensesEntity.setStatus(FeePayment.PAID);
        } else if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            schoolExpensesEntity.setStatus(FeePayment.PENDING);
        } else {
            schoolExpensesEntity.setStatus(FeePayment.PARTIAL);
        }

        schoolExpensesEntity.setPendingAmount(pendingAmount);
        schoolExpensesEntity.setTotal(total);
        schoolExpensesEntity.setPaidAmount(paidAmount);
        schoolExpensesEntity.setAcademicYear(schoolExpensesDTO.getAcademicYear());

        //set document
        if (schoolExpensesDTO.getDocument() !=null){
           schoolExpensesEntity.setDocument(Base64.getDecoder().decode(schoolExpensesEntity.getDocument()));
        }

        // set vendor
        VendorMasterEntity vendorMaster = vendorMasterRepository.findById(schoolExpensesDTO.getVendorMasterId()).get();
        schoolExpensesEntity.setVendorMasterEntity(vendorMaster);

        //set purchase
        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesDTO.getPurchaseId()).get();
        schoolExpensesEntity.setPurchaseEntity(purchaseEntity);

        // set audit details
        schoolExpensesEntity.setAuditDetails(addAuditDetails(schoolExpensesEntity.getAuditDetails()));

        //save
        SchoolExpensesEntity savedSchoolExpensesEntity = schoolExpensesRepository.save(schoolExpensesEntity);

        // Convert to DTO
        SchoolExpensesDTO expensesDTO = modelMapper.map(savedSchoolExpensesEntity, SchoolExpensesDTO.class);
        expensesDTO.setDocument(savedSchoolExpensesEntity.getDocument());

        VendorMasterDTO vendorMasterDTO = modelMapper.map(savedSchoolExpensesEntity.getVendorMasterEntity(), VendorMasterDTO.class);
        expensesDTO.setVendorMasterDTO(vendorMasterDTO);
        expensesDTO.setVendorMasterId(vendorMasterDTO.getVendorMasterId());

        PurchaseDTO purchaseDTO = modelMapper.map(savedSchoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);
        expensesDTO.setPurchaseDTO(purchaseDTO);
        expensesDTO.setPurchaseId(purchaseDTO.getPurchaseId());

        log.info("Exit from saveSchoolExpenses");

        return expensesDTO;
    }

    @Override
    public SchoolExpensesDTO getSchoolExpenses(Long schoolExpenseId) {
        log.info("Enter into getSchoolExpenses");

        if (schoolExpenseId == null) {
            throw new CustomException("Expense ID can not be null", HttpStatus.BAD_REQUEST);
        }

        SchoolExpensesEntity schoolExpensesEntity = schoolExpensesRepository.findById(schoolExpenseId)
                .orElseThrow(() -> new CustomException("Product not found with this id", HttpStatus.NOT_FOUND));


        //set document
        if (schoolExpensesEntity.getDocument()!=null){

            schoolExpensesEntity.setDocument(Base64.getDecoder().decode(schoolExpensesEntity.getDocument()));
        }

        // set vendor
        VendorMasterEntity vendorMaster = vendorMasterRepository.findById(schoolExpensesEntity.getVendorMasterEntity().getVendorMasterId()).get();
        schoolExpensesEntity.setVendorMasterEntity(vendorMaster);

        //set purchase
        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesEntity.getPurchaseEntity().getPurchaseId()).get();
        schoolExpensesEntity.setPurchaseEntity(purchaseEntity);

        // convert to DTO
        SchoolExpensesDTO schoolExpensesDTO = modelMapper.map(schoolExpensesEntity, SchoolExpensesDTO.class);

        schoolExpensesDTO.setDocument(schoolExpensesEntity.getDocument());

        VendorMasterDTO vendorMasterDTO = modelMapper.map(schoolExpensesEntity.getVendorMasterEntity(), VendorMasterDTO.class);
        schoolExpensesDTO.setVendorMasterDTO(vendorMasterDTO);
        schoolExpensesDTO.setVendorMasterId(vendorMasterDTO.getVendorMasterId());

        PurchaseDTO purchaseDTO = modelMapper.map(schoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);
        schoolExpensesDTO.setPurchaseDTO(purchaseDTO);
        schoolExpensesDTO.setPurchaseId(purchaseDTO.getPurchaseId());

        log.info("Exit from getSchoolExpenses");
        return schoolExpensesDTO;
    }

    @Override
    public SchoolExpensesDTO updateSchoolExpenses(SchoolExpensesDTO schoolExpensesDTO) {
        log.info("Enter into updateSchoolExpenses");

        if (schoolExpensesDTO == null) {
            throw new CustomException("Product information can not be null", HttpStatus.PRECONDITION_FAILED);
        }
        SchoolExpensesEntity existingSchoolExpensesEntity = schoolExpensesRepository.findById(schoolExpensesDTO.getSchoolExpenseId())
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));

        // Calculate pending amount automatically
        BigDecimal total = schoolExpensesDTO.getTotal() != null
                ? schoolExpensesDTO.getTotal()
                : BigDecimal.ZERO;

        BigDecimal paidAmount = schoolExpensesDTO.getPaidAmount() != null
                ? schoolExpensesDTO.getPaidAmount()
                : BigDecimal.ZERO;

        BigDecimal pendingAmount = total.subtract(paidAmount);


        if (paidAmount.compareTo(total) == 0) {
            existingSchoolExpensesEntity.setStatus(FeePayment.PAID);
        } else if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            existingSchoolExpensesEntity.setStatus(FeePayment.PENDING);
        } else {
            existingSchoolExpensesEntity.setStatus(FeePayment.PARTIAL);
        }

        //update
        existingSchoolExpensesEntity.setQuantity(schoolExpensesDTO.getQuantity());
        existingSchoolExpensesEntity.setPrice(schoolExpensesDTO.getPrice());
        existingSchoolExpensesEntity.setPendingAmount(pendingAmount);
        existingSchoolExpensesEntity.setPaidAmount(paidAmount);
        existingSchoolExpensesEntity.setTotal(total);
        existingSchoolExpensesEntity.setPurchaseDate(schoolExpensesDTO.getPurchaseDate());
        existingSchoolExpensesEntity.setAcademicYear(schoolExpensesDTO.getAcademicYear());


        //set document
        if (schoolExpensesDTO.getDocument() !=null){
            existingSchoolExpensesEntity.setDocument(Base64.getDecoder().decode(existingSchoolExpensesEntity.getDocument()));
        } else {
            existingSchoolExpensesEntity.setDocument(null);
        }

        // set vendor
        VendorMasterEntity vendorMaster = vendorMasterRepository.findById(schoolExpensesDTO.getVendorMasterId()).get();
        existingSchoolExpensesEntity.setVendorMasterEntity(vendorMaster);

        //set purchase
        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesDTO.getPurchaseId()).get();
        existingSchoolExpensesEntity.setPurchaseEntity(purchaseEntity);


        // set audit details
        existingSchoolExpensesEntity.setAuditDetails(addAuditDetails(existingSchoolExpensesEntity.getAuditDetails()));

        //save
        SchoolExpensesEntity updatedSchoolExpensesEntity = schoolExpensesRepository.save(existingSchoolExpensesEntity);

        //convert to DTO
        SchoolExpensesDTO expensesDTO = modelMapper.map(updatedSchoolExpensesEntity, SchoolExpensesDTO.class);

        SchoolExpensesDTO schoolExpensesDTO1 = modelMapper.map(updatedSchoolExpensesEntity, SchoolExpensesDTO.class);
        expensesDTO.setDocument(schoolExpensesDTO1.getDocument());

        VendorMasterDTO vendorMasterDTO = modelMapper.map(updatedSchoolExpensesEntity.getVendorMasterEntity(), VendorMasterDTO.class);
        expensesDTO.setVendorMasterDTO(vendorMasterDTO);
        expensesDTO.setVendorMasterId(vendorMasterDTO.getVendorMasterId());

        PurchaseDTO purchaseDTO = modelMapper.map(updatedSchoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);
        expensesDTO.setPurchaseDTO(purchaseDTO);
        expensesDTO.setPurchaseId(purchaseDTO.getPurchaseId());

        log.info("Exit from updateSchoolExpenses");
        return expensesDTO;
    }

    @Override
    public String deleteSchoolExpenses(Long schoolExpenseId) {
        log.info("Enter into deleteSchoolExpenses");

        if (schoolExpenseId == null) {
            throw new CustomException("Purchase ID can not be null", HttpStatus.BAD_REQUEST);
        }

        SchoolExpensesEntity schoolExpensesEntity = schoolExpensesRepository.findById(schoolExpenseId)
                .orElseThrow(() -> new CustomException("Purchase not found", HttpStatus.NOT_FOUND));

        schoolExpensesRepository.delete(schoolExpensesEntity);
        log.info("Exit from deleteSchoolExpenses");
        return "Data deleted successfully";
    }

    @Override
    public Map<String, Object> getAllSchoolExpensesByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllSchoolExpensesByFilter");

        Page<SchoolExpensesEntity> schoolExpensesEntityPage;
        List<SchoolExpensesEntity> schoolExpensesEntities;
        long totalElement;

        CustomQuerySpecification<SchoolExpensesEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            schoolExpensesEntityPage = schoolExpensesRepository.findAll(customQuerySpecification, pageable);
            schoolExpensesEntities = schoolExpensesEntityPage.getContent();
            totalElement = schoolExpensesEntityPage.getTotalElements();
        } else {
            schoolExpensesEntities = schoolExpensesRepository.findAll(customQuerySpecification);
            totalElement = schoolExpensesEntities.size();
        }

        List<SchoolExpensesDTO> schoolExpensesDTOS = schoolExpensesEntities.stream()
                .map(schoolExpensesEntity -> {

                    SchoolExpensesDTO schoolExpensesDTO = modelMapper.map(schoolExpensesEntity, SchoolExpensesDTO.class);


                    // set Document
                    if (schoolExpensesEntity.getDocument() !=null){
                        SchoolExpensesDTO schoolExpensesDTO1 = modelMapper.map(schoolExpensesEntity, SchoolExpensesDTO.class);
                        schoolExpensesDTO.setDocument(schoolExpensesDTO1.getDocument());
                    }
                    //set vendor
                    if (schoolExpensesEntity.getVendorMasterEntity() != null) {

                        VendorMasterDTO vendorMasterDTO =modelMapper.map(schoolExpensesEntity.getVendorMasterEntity(),VendorMasterDTO.class);
                        schoolExpensesDTO.setVendorMasterDTO(vendorMasterDTO);
                        schoolExpensesDTO.setVendorMasterId(vendorMasterDTO.getVendorMasterId());
                    } else {
                        schoolExpensesDTO.setVendorMasterDTO(null);
                        schoolExpensesDTO.setVendorMasterId(null);
                    }

                    // Set Purchase
                    if (schoolExpensesEntity.getPurchaseEntity() != null) {

                        PurchaseDTO purchaseDTO =modelMapper.map(schoolExpensesEntity.getPurchaseEntity(),PurchaseDTO.class);
                        schoolExpensesDTO.setPurchaseDTO(purchaseDTO);
                        schoolExpensesDTO.setPurchaseId(purchaseDTO.getPurchaseId());
                    } else {
                        schoolExpensesDTO.setPurchaseDTO(null);
                        schoolExpensesDTO.setPurchaseId(null);
                    }

                    return schoolExpensesDTO;

                })
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("SchoolExpensesDTOS", schoolExpensesDTOS);
        map.put("Total Element", totalElement);

        log.info("Exit from getAllSchoolExpensesByFilter");

        return map;
    }

    @Override
    public Map<String, Object> getSchoolExpensesReportData(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getSchoolExpensesReportData");

        String startYear = String.valueOf(getStartDate().getYear());
        String endYear = String.valueOf(getEndDate().getYear());

        String academicYear = startYear.concat("-").concat(endYear);

        filter.put("academicYear", academicYear);

        Page<SchoolExpensesEntity> purchaseEntityPage;
        List<SchoolExpensesEntity> purchaseEntities;
        long totalElement;

        CustomQuerySpecification<SchoolExpensesEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            purchaseEntityPage = schoolExpensesRepository.findAll(customQuerySpecification, pageable);
            purchaseEntities = purchaseEntityPage.getContent();
            totalElement = purchaseEntityPage.getTotalElements();
        } else {
            purchaseEntities = schoolExpensesRepository.findAll(customQuerySpecification);
            totalElement = purchaseEntities.size();
        }

        List<SchoolExpensesReportDTO> reportDTOList = purchaseEntities.stream()
                .map(purchaseEntity -> {
                    SchoolExpensesReportDTO reportDTO = modelMapper.map(purchaseEntity, SchoolExpensesReportDTO.class);

                    return reportDTO;
                })
                .toList();

        Map<String, Object> result = new HashMap<>();

        result.put("Data", reportDTOList);
        result.put("total", totalElement);
        log.info("Exit from getSchoolExpensesReportData");

        return result;
    }
}
