package com.flint.sample_be_springboot.service;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, Map<String, Long>> getAllStudentsCount();

    Map<String, Long> getAllUsersCount();

    Map<String, Long> getAllAdmissionInquiryCount();

    Map<String, Long> getAllExpensesCount(String academicYear);

    Map<String, BigDecimal> getAllPaidExpensesTotal(String academicYear);

    Map<String, BigDecimal> getAllExpensesTotal(String academicYear);

    Map<String, Long> getAllTotalPaidExpensesCountAndTotalExpensesCount(String academicYear);


}
