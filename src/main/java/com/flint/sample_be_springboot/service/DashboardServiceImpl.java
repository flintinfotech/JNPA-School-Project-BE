package com.flint.sample_be_springboot.service;


import com.flint.sample_be_springboot.entity.AdmissionInquiry;
import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.UserEntity;
import com.flint.sample_be_springboot.entity.student.AcademicInformationEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.enums.Role;
import com.flint.sample_be_springboot.enums.StudentStatus;
import com.flint.sample_be_springboot.repository.AdmissionInquiryRepository;
import com.flint.sample_be_springboot.repository.EmployeeDetailsRepository;
import com.flint.sample_be_springboot.repository.UserRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.repository.websiteModuleRepository.NewsRepository;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class DashboardServiceImpl extends BaseService implements DashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdmissionInquiryRepository admissionInquiryRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository ;


    public Map<String, Map<String, Long>> getAllStudentsCount() {

        Map<Integer, Map<String, Long>> numericMap = new TreeMap<>();
        Map<String, Map<String, Long>> result = new LinkedHashMap<>();

        List<StudentEntity> students = studentRepository.findAll();

        long totalBoys = 0;
        long totalGirls = 0;

        for (StudentEntity student : students) {


            if (student.getStatus() != StudentStatus.ACTIVE) {
                continue;
            }

            String gender = student.getGender();

            if (gender == null) continue;

            gender = gender.trim().toLowerCase();

            if (gender.equals("male")) totalBoys++;
            else if (gender.equals("female")) totalGirls++;

            if (student.getAcademicInformationEntity() != null &&
                    !student.getAcademicInformationEntity().isEmpty()) {

                for (AcademicInformationEntity academic : student.getAcademicInformationEntity()) {

                    String standard = academic.getStandard();

                    if (standard == null || standard.trim().isEmpty()) continue;

                    standard = normalizeStandard(standard);

                    Integer number = extractNumber(standard);

                    if (number != null) {
                        Map<String, Long> countMap =
                                numericMap.getOrDefault(number, createEmptyMap());

                        updateGenderCount(countMap, gender);

                        numericMap.put(number, countMap);

                    } else {
                        Map<String, Long> countMap =
                                result.getOrDefault(standard, createEmptyMap());

                        updateGenderCount(countMap, gender);

                        result.put(standard, countMap);
                    }
                }
            }
        }

        Map<String, Map<String, Long>> finalResult = new LinkedHashMap<>();

        // Ordered non-numeric
        if (result.containsKey("Playgroup"))
            finalResult.put("Playgroup", result.remove("Playgroup"));

        if (result.containsKey("Nursery"))
            finalResult.put("Nursery", result.remove("Nursery"));

        if (result.containsKey("LKG"))
            finalResult.put("LKG", result.remove("LKG"));

        if (result.containsKey("UKG"))
            finalResult.put("UKG", result.remove("UKG"));

        finalResult.putAll(result);

        // Add numeric (1st, 2nd...)
        for (Map.Entry<Integer, Map<String, Long>> entry : numericMap.entrySet()) {
            finalResult.put(toOrdinal(entry.getKey()), entry.getValue());
        }

        // Total
        Map<String, Long> totalMap = new HashMap<>();
        totalMap.put("boys", totalBoys);
        totalMap.put("girls", totalGirls);

        finalResult.put("Total", totalMap);

        return finalResult;
    }

    private void updateGenderCount(Map<String, Long> map, String gender) {
        if ("male".equals(gender)) {
            map.put("boys", map.getOrDefault("boys", 0L) + 1);
        } else if ("female".equals(gender)) {
            map.put("girls", map.getOrDefault("girls", 0L) + 1);
        }
    }

    private Map<String, Long> createEmptyMap() {
        Map<String, Long> map = new HashMap<>();
        map.put("boys", 0L);
        map.put("girls", 0L);
        return map;
    }

    private String normalizeStandard(String standard) {
        if (standard == null) return null;

        standard = standard.trim();

        if (standard.equalsIgnoreCase("Junior KG (LKG)")) return "LKG";
        if (standard.equalsIgnoreCase("Senior KG (UKG)")) return "UKG";

        return standard;
    }

    private String toOrdinal(int number) {
        if (number >= 11 && number <= 13) return number + "th";

        switch (number % 10) {
            case 1:
                return number + "st";
            case 2:
                return number + "nd";
            case 3:
                return number + "rd";
            default:
                return number + "th";
        }
    }

    private Integer extractNumber(String value) {
        try {
            String num = value.replaceAll("[^0-9]", "");
            return num.isEmpty() ? null : Integer.parseInt(num);
        } catch (Exception e) {
            return null;
        }
    }


    public Map<String, Long> getAllUsersCount() {
        Map<String, Long> map = new HashMap<>();
        System.err.println(getStartDate());
        System.err.println(getEndDate());
        List<EmployeeDetailsEntity> entities = employeeDetailsRepository.findCurrentWorkingUsersByAcademicYear(getStartDate(),getEndDate());


        Long count = 0l;

        for (EmployeeDetailsEntity entity : entities) {
            if (!Role.ADMIN.toString().equals(entity.getRole().toString())
                    && !Role.PRINCIPAL.toString().equals(entity.getRole().toString()) &&
                    !Role. STUDENT.toString().equals(entity.getRole().toString())) {
                if (map.containsKey(entity.getRole().toString())) {
                    map.put(entity.getRole().toString(), map.get(entity.getRole().toString()) + 1);
                } else {
                    map.put(entity.getRole().toString(), 1L);
                }
            }
        }
        return map;
    }

    public Map<String, Long> getAllAdmissionInquiryCount() {
        Map<String, Long> map = new HashMap<>();
        List<AdmissionInquiry> inquiries = admissionInquiryRepository.findAll();
        Long total = 0L;

        for (AdmissionInquiry inquiry : inquiries) {
            total++;

            String status = inquiry.getStatus().toString();
            map.put(status, map.getOrDefault(status, 0L) + 1);
        }
        map.put("Total", total);
        return map;
    }


}
