package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeDetailsDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface EmployeeDetailsService {

    EmployeeDetailsDTO getEmployeeDetailsById(Long employeeId);

    EmployeeDetailsDTO getEmployeeDetailsByUserId(Long userId);

    Map<String, Object> saveEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO);

    EmployeeDetailsDTO updateEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO);

    String deleteEmployeeDetails(Long employeeDetailsId);

    Map<String, Object> getAllEmployeeDetailsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
