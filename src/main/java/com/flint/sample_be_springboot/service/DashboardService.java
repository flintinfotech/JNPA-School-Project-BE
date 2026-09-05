package com.flint.sample_be_springboot.service;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, Map<String, Long>> getAllStudentsCount();

    Map<String, Long> getAllUsersCount();

    Map<String, Long> getAllAdmissionInquiryCount();

    Map<String, Long> getAllExpensesCount();

    Map<String, BigDecimal> getAllPaidExpensesTotal();

    Map<String, BigDecimal> getAllExpensesTotal();

    Map<String, Long> getAllTotalPaidExpensesCountAndTotalExpensesCount();



}
