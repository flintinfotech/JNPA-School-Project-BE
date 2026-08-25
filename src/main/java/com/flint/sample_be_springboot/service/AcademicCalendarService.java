package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.AcademicCalendarDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AcademicCalendarService {
    AcademicCalendarDTO getAcademicCalendarEventById(Long academicCalendarId);

    AcademicCalendarDTO saveAcademicCalendarEvent(AcademicCalendarDTO academicCalendarDTO);

    AcademicCalendarDTO updateAcademicCalendarEvent(AcademicCalendarDTO academicCalendarDTO);

    String deleteAcademicCalendarEvent(Long academicCalendarId);

    Map<String, Object> getAllAcademicCalendarEventsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
