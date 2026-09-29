package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeSalaryDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface EmployeeSalaryService {

    EmployeeSalaryDTO getEmployeeSalary(Long employeeDetailsId);

    EmployeeSalaryDTO saveEmployeeSalary(EmployeeSalaryDTO employeeSalaryDTO);

    EmployeeSalaryDTO updateEmployeeSalary(EmployeeSalaryDTO employeeSalaryDTO);

    String deleteEmployeeSalary(Long employeeDetailsId);

    Map<String, Object> getAllEmployeeSalaryByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
