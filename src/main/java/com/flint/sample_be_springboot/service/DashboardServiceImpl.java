package com.flint.sample_be_springboot.service;


import com.flint.sample_be_springboot.entity.AdmissionInquiry;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.entity.websiteModuleEntities.NewsEntity;
import com.flint.sample_be_springboot.enums.Role;
import com.flint.sample_be_springboot.repository.AdmissionInquiryRepository;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.repository.websiteModuleRepository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdmissionInquiryRepository admissionInquiryRepository;


    @Autowired
    private NewsRepository newsRepository;

    public Map<String, Long> getAllStudentsCount() {
        Map<String, Long> map = new HashMap<>();
        List<StudentEntity> students = studentRepository.findAll();
        long total = 0L;

        for (StudentEntity student : students) {
            total++;

            String gender = student.getGender().toString();
            if (map.containsKey(gender)) {
                map.put(gender, map.get(gender) + 1);
            } else {
                map.put(gender, 1L);
            }
        }

        map.put("Total", total);
        return map;
    }

    public Map<String, Long> getAllUsersCount() {
        Map<String, Long> map = new HashMap<>();
        List<UserEntity> entities = userRepository.findAll();

        Long count = 0l;

        for (UserEntity entity : entities) {
            if (!Role.ADMIN.toString().equals(entity.getRole().toString())
                    && !Role.PRINCIPAL.toString().equals(entity.getRole().toString()) &&
                    !Role.STUDENT.toString().equals(entity.getRole().toString())) {
                if (map.containsKey(entity.getRole().toString())) {
                    map.put(entity.getRole().toString(), map.get(entity.getRole().toString()) + 1);
                } else {
                    map.put(entity.getRole().toString(), 1l);
                }
            }
        }
        return map;
    }

    public Map<String, Long> getAllAdmissionInquiryCount()
    {
        Map<String, Long> map = new HashMap<>();
      List<AdmissionInquiry> inquiries =  admissionInquiryRepository.findAll();
      Long total=0L;

      for (AdmissionInquiry inquiry:inquiries)
      {
          total++;

          String status = inquiry.getStatus().toString();
          map.put(status,map.getOrDefault(status,0L) + 1);

      }
      map.put("Total", total);
      return map;
    }







}
