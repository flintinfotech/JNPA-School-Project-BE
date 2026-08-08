package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.entity.websiteModuleEntities.NewsEntity;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    Map<String, Long> getAllStudentsCount();

    Map<String, Long> getAllUsersCount();

    Map<String, Long> getAllAdmissionInquiryCount();



}
