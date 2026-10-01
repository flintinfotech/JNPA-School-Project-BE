package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.PurchaseDTO;
import com.flint.sample_be_springboot.dto.SchoolExpensesDTO;
import com.flint.sample_be_springboot.dto.SchoolExpensesReportDTO;
import com.flint.sample_be_springboot.dto.SchoolExpensesReportDataDTO;
import com.flint.sample_be_springboot.entity.PurchaseEntity;
import com.flint.sample_be_springboot.entity.SchoolExpensesEntity;
import com.flint.sample_be_springboot.enums.FeePayment;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.PurchaseRepository;
import com.flint.sample_be_springboot.repository.SchoolExpensesRepository;
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
    private PurchaseRepository purchaseRepository;

    @Override
    public SchoolExpensesDTO saveSchoolExpenses(SchoolExpensesDTO schoolExpensesDTO) {
        log.info("Enter into saveSchoolExpenses");

        if (schoolExpensesDTO == null) {
            throw new CustomException("Expenses information can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        SchoolExpensesEntity schoolExpensesEntity = modelMapper.map(schoolExpensesDTO, SchoolExpensesEntity.class);

        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesDTO.getPurchaseId())
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.PRECONDITION_FAILED));

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
        schoolExpensesEntity.setPurchaseEntity(purchaseEntity);
        schoolExpensesEntity.setAcademicYear(schoolExpensesDTO.getAcademicYear());


        //save
        SchoolExpensesEntity savedSchoolExpensesEntity = schoolExpensesRepository.save(schoolExpensesEntity);
        PurchaseDTO purchaseDTO = modelMapper.map(savedSchoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);

        // Convert to DTO
        SchoolExpensesDTO expensesDTO = modelMapper.map(savedSchoolExpensesEntity, SchoolExpensesDTO.class);
        expensesDTO.setPurchaseDTO(purchaseDTO);

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

        SchoolExpensesDTO schoolExpensesDTO = modelMapper.map(schoolExpensesEntity, SchoolExpensesDTO.class);

        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesDTO.getPurchaseId())
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.PRECONDITION_FAILED));

        schoolExpensesEntity.setPurchaseEntity(purchaseEntity);

        PurchaseDTO purchaseDTO = modelMapper.map(schoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);
        schoolExpensesDTO.setPurchaseDTO(purchaseDTO);


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

        PurchaseEntity purchaseEntity = purchaseRepository.findById(schoolExpensesDTO.getPurchaseId())
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.PRECONDITION_FAILED));


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

        existingSchoolExpensesEntity.setPurchaseEntity(purchaseEntity);

        //update
        existingSchoolExpensesEntity.setQuantity(schoolExpensesDTO.getQuantity());
        existingSchoolExpensesEntity.setPrice(schoolExpensesDTO.getPrice());
        existingSchoolExpensesEntity.setPendingAmount(pendingAmount);
        existingSchoolExpensesEntity.setPaidAmount(paidAmount);
        existingSchoolExpensesEntity.setTotal(total);
        existingSchoolExpensesEntity.setPurchaseDate(schoolExpensesDTO.getPurchaseDate());
        existingSchoolExpensesEntity.setAcademicYear(schoolExpensesDTO.getAcademicYear());

        //save
        SchoolExpensesEntity updatedSchoolExpensesEntity = schoolExpensesRepository.save(existingSchoolExpensesEntity);
        PurchaseDTO purchaseDTO = modelMapper.map(updatedSchoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);

        //convert to DTO
        SchoolExpensesDTO expensesDTO = modelMapper.map(updatedSchoolExpensesEntity, SchoolExpensesDTO.class);
        expensesDTO.setPurchaseDTO(purchaseDTO);

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

                    // PurchaseEntity -> PurchaseDTO
                    if (schoolExpensesEntity.getPurchaseEntity() != null) {

                        PurchaseDTO purchaseDTO = modelMapper.map(schoolExpensesEntity.getPurchaseEntity(), PurchaseDTO.class);
                        schoolExpensesDTO.setPurchaseDTO(purchaseDTO);

                        // Set purchaseId
                        schoolExpensesDTO.setPurchaseId(schoolExpensesEntity.getPurchaseEntity().getPurchaseId());
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

        final String academicYear = filter != null && filter.get("academicYear") != null
                ? filter.get("academicYear").toString()
                : null;

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

        List<SchoolExpensesReportDTO> reportDTOList = purchaseEntities.stream()
                .map(purchaseEntity -> {
                    SchoolExpensesReportDTO reportDTO = new SchoolExpensesReportDTO();

                    reportDTO.setProductCode(purchaseEntity.getProductCode());
                    reportDTO.setProductName(purchaseEntity.getProductName());
                    reportDTO.setCategory(purchaseEntity.getCategory());

                    List<SchoolExpensesReportDataDTO> reportDataDTOList = purchaseEntity.getSchoolExpensesEntities()
                            .stream()

                            // Academic year filter
                            .filter(expenseEntity ->
                                    academicYear == null || academicYear.isBlank() || academicYear.equals(expenseEntity.getAcademicYear()))

                            .map(expenseEntity ->
                                    modelMapper.map(expenseEntity, SchoolExpensesReportDataDTO.class))
                            .toList();

                    reportDTO.setReportDataDTOList(reportDataDTOList);
                    return reportDTO;
                })

                // Remove products having no matching expense
                .filter(reportDTO ->
                        reportDTO.getReportDataDTOList() != null && !reportDTO.getReportDataDTOList().isEmpty())
                .toList();

        Map<String, Object> result = new HashMap<>();

        result.put("Data", reportDTOList);
        result.put("total", reportDTOList.size());
        log.info("Exit from getSchoolExpensesReportData");

        return result;
    }
}
