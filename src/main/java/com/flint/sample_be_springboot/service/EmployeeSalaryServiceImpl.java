package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeSalaryDTO;
import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.EmployeeSalaryEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.EmployeeDetailsRepository;
import com.flint.sample_be_springboot.repository.EmployeeSalaryRepository;
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

@Service
@Slf4j
public class EmployeeSalaryServiceImpl extends BaseService implements EmployeeSalaryService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Override
    public EmployeeSalaryDTO saveEmployeeSalary(EmployeeSalaryDTO employeeSalaryDTO) {
        log.info("Enter into saveEmployeeSalary");

        if (employeeSalaryDTO == null) {
            throw new CustomException("Employee salary can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (employeeSalaryDTO.getEmployeeDetailsId() == null) {
            throw new CustomException("Employee details id can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(employeeSalaryDTO.getEmployeeDetailsId())
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        EmployeeSalaryEntity employeeSalaryEntity = modelMapper.map(employeeSalaryDTO, EmployeeSalaryEntity.class);

        employeeSalaryEntity.setEmployeeDetailsEntity(employeeDetailsEntity);

        BigDecimal basicSalary = employeeSalaryDTO.getBasicSalary() != null
                ? employeeSalaryDTO.getBasicSalary()
                : BigDecimal.ZERO;

        BigDecimal hra = employeeSalaryDTO.getHra() != null
                ? employeeSalaryDTO.getHra()
                : BigDecimal.ZERO;

        BigDecimal transportAllowance = employeeSalaryDTO.getTransportAllowance() != null
                ? employeeSalaryDTO.getTransportAllowance()
                : BigDecimal.ZERO;

        BigDecimal medicalAllowance = employeeSalaryDTO.getMedicalAllowance() != null
                ? employeeSalaryDTO.getMedicalAllowance()
                : BigDecimal.ZERO;

        BigDecimal otherAllowance = employeeSalaryDTO.getOtherAllowance() != null
                ? employeeSalaryDTO.getOtherAllowance()
                : BigDecimal.ZERO;

        BigDecimal deduction = employeeSalaryDTO.getDeduction() != null
                ? employeeSalaryDTO.getDeduction()
                : BigDecimal.ZERO;

        BigDecimal netSalary = basicSalary
                .add(hra)
                .add(transportAllowance)
                .add(medicalAllowance)
                .add(otherAllowance)
                .subtract(deduction);

        employeeSalaryEntity.setNetSalary(netSalary);

        employeeSalaryRepository.save(employeeSalaryEntity);

        EmployeeSalaryDTO salaryDTO = modelMapper.map(employeeSalaryEntity, EmployeeSalaryDTO.class);

        salaryDTO.setEmployeeDetailsId(employeeDetailsEntity.getEmployeeDetailsId());

        log.info("Exit from saveEmployeeSalary");
        return salaryDTO;
    }

    @Override
    public EmployeeSalaryDTO getEmployeeSalary(Long employeeDetailsId) {

        log.info("Enter into getEmployeeSalary");

        if (employeeDetailsId == null) {
            throw new CustomException("Employee details id can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        EmployeeSalaryEntity employeeSalaryEntity = employeeSalaryRepository
                .findByEmployeeDetailsEntity_EmployeeDetailsId(employeeDetailsId)
                .orElseThrow(() -> new CustomException("Employee salary is not exist", HttpStatus.NOT_FOUND));

        EmployeeSalaryDTO salaryDTO = modelMapper.map(employeeSalaryEntity, EmployeeSalaryDTO.class);

        salaryDTO.setEmployeeDetailsId(employeeDetailsId);

        log.info("Exit from getEmployeeSalary");
        return salaryDTO;
    }

    @Override
    public EmployeeSalaryDTO updateEmployeeSalary(EmployeeSalaryDTO employeeSalaryDTO) {
        log.info("Enter into updateEmployeeSalary");

        if (employeeSalaryDTO == null) {
            throw new CustomException("Employee salary can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        EmployeeSalaryEntity existingEmployeeSalaryEntity = employeeSalaryRepository.findById(employeeSalaryDTO.getEmployeeSalaryId())
                .orElseThrow(() -> new CustomException("Employee salary is not exist", HttpStatus.NOT_FOUND));

        // update
        existingEmployeeSalaryEntity.setBasicSalary(employeeSalaryDTO.getBasicSalary());
        existingEmployeeSalaryEntity.setSalaryDate(employeeSalaryDTO.getSalaryDate());
        existingEmployeeSalaryEntity.setHra(employeeSalaryDTO.getHra());
        existingEmployeeSalaryEntity.setTransportAllowance(employeeSalaryDTO.getTransportAllowance());
        existingEmployeeSalaryEntity.setMedicalAllowance(employeeSalaryDTO.getMedicalAllowance());
        existingEmployeeSalaryEntity.setOtherAllowance(employeeSalaryDTO.getOtherAllowance());
        existingEmployeeSalaryEntity.setDeduction(employeeSalaryDTO.getDeduction());
        existingEmployeeSalaryEntity.setNetSalary(employeeSalaryDTO.getNetSalary());
        existingEmployeeSalaryEntity.setRemark(employeeSalaryDTO.getRemark());
        existingEmployeeSalaryEntity.setAcademicYear(employeeSalaryDTO.getAcademicYear());
        existingEmployeeSalaryEntity.setAuditDetails(addAuditDetails(existingEmployeeSalaryEntity.getAuditDetails()));

        BigDecimal netSalary = employeeSalaryDTO.getBasicSalary()
                .add(employeeSalaryDTO.getHra())
                .add(employeeSalaryDTO.getTransportAllowance())
                .add(employeeSalaryDTO.getMedicalAllowance())
                .add(employeeSalaryDTO.getOtherAllowance())
                .subtract(employeeSalaryDTO.getDeduction());

        existingEmployeeSalaryEntity.setNetSalary(netSalary);

        EmployeeSalaryEntity updatedEmployeeSalaryEntity = employeeSalaryRepository.save(existingEmployeeSalaryEntity);

        //return dto
        EmployeeSalaryDTO salaryDTO = modelMapper.map(updatedEmployeeSalaryEntity, EmployeeSalaryDTO.class);

        log.info("Exit from updateEmployeeSalary");
        return salaryDTO;
    }

    @Override
    public String deleteEmployeeSalary(Long employeeDetailsId) {

        log.info("Enter into deleteEmployeeSalary");

        if (employeeDetailsId == null) {
            throw new CustomException("Employee details id can not be null", HttpStatus.PRECONDITION_FAILED);
        }

        EmployeeSalaryEntity employeeSalaryEntity = employeeSalaryRepository
                .findByEmployeeDetailsEntity_EmployeeDetailsId(employeeDetailsId)
                .orElseThrow(() -> new CustomException("Employee salary does not exist", HttpStatus.NOT_FOUND));

        employeeSalaryRepository.delete(employeeSalaryEntity);

        log.info("Exit from deleteEmployeeSalary");
        return "Employee salary deleted successfully";
    }

    @Override
    public Map<String, Object> getAllEmployeeSalaryByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllEmployeeSalaryByFilter");

        Page<EmployeeSalaryEntity> employeeSalaryEntityPage;
        List<EmployeeSalaryEntity> employeeSalaryEntities;
        long totalElement;

        CustomQuerySpecification<EmployeeSalaryEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            employeeSalaryEntityPage = employeeSalaryRepository.findAll(customQuerySpecification, pageable);
            employeeSalaryEntities = employeeSalaryEntityPage.getContent();
            totalElement = employeeSalaryEntityPage.getTotalElements();
        } else {
            employeeSalaryEntities = employeeSalaryRepository.findAll(customQuerySpecification);
            totalElement = employeeSalaryEntities.size();
        }

        List<EmployeeSalaryDTO> employeeSalaryDTOs =
                employeeSalaryEntities.stream()
                        .map(employeeSalaryEntity -> {

                            EmployeeSalaryDTO salaryDTO = modelMapper.map(employeeSalaryEntity, EmployeeSalaryDTO.class);

                            if (employeeSalaryEntity.getEmployeeDetailsEntity() != null) {
                                salaryDTO.setEmployeeDetailsId(employeeSalaryEntity.getEmployeeDetailsEntity().getEmployeeDetailsId());
                            }

                            return salaryDTO;
                        })
                        .toList();

        log.info("Exit from getAllEmployeeSalaryByFilter");
        Map<String, Object> result = new HashMap<>();
        result.put("Data", employeeSalaryDTOs);
        result.put("total", totalElement);
        return result;
    }
}
