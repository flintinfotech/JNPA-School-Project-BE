package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.classRoom.AcademicYearDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AcademicYearService {

    AcademicYearDTO saveAcademicYear(AcademicYearDTO academicYearDTO);

    AcademicYearDTO updateAcademicYear(AcademicYearDTO academicYearDTO);

    AcademicYearDTO getAcademicYearById(Long academicYearId);

    String deleteAcademicYear(Long academicYearId);

    Map<String, Object> getAllAcademicYearsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

    AcademicYearDTO getCurrentAcademicYear();

    AcademicYearDTO setCurrentAcademicYear(Long academicYearId);

}
