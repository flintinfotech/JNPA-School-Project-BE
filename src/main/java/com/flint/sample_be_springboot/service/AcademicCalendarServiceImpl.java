package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.AcademicCalendarDTO;
import com.flint.sample_be_springboot.entity.AcademicCalendarEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.AcademicCalendarRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcademicCalendarServiceImpl extends BaseService implements AcademicCalendarService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AcademicCalendarRepository academicCalendarRepository;

    @Override
    public AcademicCalendarDTO getAcademicCalendarEventById(Long academicCalendarId) {
        log.info("Enter into getAcademicCalendarEventById");

        AcademicCalendarEntity academicCalendarEntity = academicCalendarRepository.findById(academicCalendarId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        AcademicCalendarDTO academicCalendarDTO = modelMapper.map(academicCalendarEntity, AcademicCalendarDTO.class);

        log.info("Exit from getAcademicCalendarEventById");
        return academicCalendarDTO;
    }

    @Override
    public AcademicCalendarDTO saveAcademicCalendarEvent(AcademicCalendarDTO academicCalendarDTO) {
        log.info("Enter into saveAcademicCalendarEvent");

        AcademicCalendarEntity academicCalendarEntity = academicCalendarRepository
                .findByStartDateBetween(academicCalendarDTO.getStartDate(), academicCalendarDTO.getStartDate()).orElse(null);

        if (academicCalendarEntity != null) {
            throw new CustomException("Other event found on these dates", HttpStatus.FOUND);
        }

        AcademicCalendarEntity academicCalendar = modelMapper.map(academicCalendarDTO, AcademicCalendarEntity.class);

        AcademicCalendarEntity savedEntity = academicCalendarRepository.save(academicCalendar);

        AcademicCalendarDTO savedDTO = modelMapper.map(savedEntity, AcademicCalendarDTO.class);

        log.info("Exit from saveAcademicCalendarEvent");
        return savedDTO;
    }

    @Override
    public AcademicCalendarDTO updateAcademicCalendarEvent(AcademicCalendarDTO academicCalendarDTO) {
        log.info("Enter into updateAcademicCalendarEvent");

        AcademicCalendarEntity existingAcademicCalendarEntity = academicCalendarRepository.findById(academicCalendarDTO.getAcademicCalendarId())
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.FOUND));

        AcademicCalendarEntity academicCalendarEntity = academicCalendarRepository.findByStartDateBetweenAndAcademicCalendarIdNot
                (academicCalendarDTO.getStartDate(), academicCalendarDTO.getStartDate(), academicCalendarDTO.getAcademicCalendarId()).orElse(null);
        if (academicCalendarEntity != null) {
            throw new CustomException("Other event found on these dates", HttpStatus.NOT_FOUND);
        }

        modelMapper.map(academicCalendarDTO, existingAcademicCalendarEntity);

        AcademicCalendarEntity updatedEntity = academicCalendarRepository.save(existingAcademicCalendarEntity);

        AcademicCalendarDTO updatedAcademicCalendarDTO = modelMapper.map(updatedEntity, AcademicCalendarDTO.class);

        log.info("Exit from saveAcademicCalendarEvent");
        return updatedAcademicCalendarDTO;
    }

    @Override
    public String deleteAcademicCalendarEvent(Long academicCalendarId) {
        log.info("Enter into deleteAcademicCalendarEvent");

        AcademicCalendarEntity existingAcademicCalendarEntity = academicCalendarRepository.findById(academicCalendarId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        academicCalendarRepository.delete(existingAcademicCalendarEntity);

        log.info("Exit from deleteAcademicCalendarEvent");
        return "Academic calendar event deleted successfully";
    }

    @Override
    public Map<String, Object> getAllAcademicCalendarEventsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        Page<AcademicCalendarEntity> academicCalendarEntityPage;
        List<AcademicCalendarEntity> academicCalendarEntities;
        long totalElement;

        CustomQuerySpecification<AcademicCalendarEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            academicCalendarEntityPage = academicCalendarRepository.findAll(customQuerySpecification, pageable);
            academicCalendarEntities = academicCalendarEntityPage.getContent();
            totalElement = academicCalendarEntityPage.getTotalElements();
        } else {
            academicCalendarEntities = academicCalendarRepository.findAll(customQuerySpecification);
            totalElement = academicCalendarEntities.size();
        }

        List<AcademicCalendarDTO> academicCalendarDTOS = academicCalendarEntities.stream()
                .map(ac -> modelMapper.map(ac, AcademicCalendarDTO.class))
                .collect(Collectors.toUnmodifiableList());

        Map<String, Object> map = new HashMap<>();
        map.put("Academic calendar events", academicCalendarDTOS);
        map.put("total events", totalElement);

        return map;
    }
}
