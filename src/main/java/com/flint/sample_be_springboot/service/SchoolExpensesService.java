package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.SchoolExpensesDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface SchoolExpensesService {

    SchoolExpensesDTO saveSchoolExpenses(SchoolExpensesDTO schoolExpensesDTO);

    SchoolExpensesDTO getSchoolExpenses(Long schoolExpenseId);

    SchoolExpensesDTO updateSchoolExpenses(SchoolExpensesDTO schoolExpensesDTO);

    String deleteSchoolExpenses(Long schoolExpenseId);

    Map<String, Object> getAllSchoolExpensesByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
