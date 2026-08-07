package com.flint.sample_be_springboot.service;

import java.util.Map;

public interface DashboardService {

    Long getAllStudentsCount();

    Map<String, Long> getAllTeachers();

}
