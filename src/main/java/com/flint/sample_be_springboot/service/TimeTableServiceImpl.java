package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.TimeTableDTO;
import com.flint.sample_be_springboot.dto.TimeTablePeriodDTO;
import com.flint.sample_be_springboot.entity.*;
import com.flint.sample_be_springboot.entity.student.StudentResultEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.*;
import com.flint.sample_be_springboot.util.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TimeTableServiceImpl extends BaseService implements TimeTableService {


    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTablePeriodRepository timeTablePeriodRepository;

    @Autowired
    private ClassMasterRepository classMasterRepository;

    @Autowired
    private SubjectMasterRepository subjectMasterRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Override
    public TimeTableDTO saveTimeTable(TimeTableDTO timeTableDTO) {

        log.info("Enter into saveTimeTable");

        if (timeTableDTO == null) {
            throw new CustomException("Time table data can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        TimeTableEntity timeTableEntity = modelMapper.map(timeTableDTO, TimeTableEntity.class);
        ClassMasterEntity classMasterEntity = classMasterRepository.findById(timeTableDTO.getClassMasterId())
                .orElseThrow(() -> new CustomException("Class not found", HttpStatus.NOT_FOUND));

        timeTableEntity.setClassMasterEntity(classMasterEntity);
        timeTableEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));


        List<TimeTablePeriodEntity> timeTablePeriodEntities = new ArrayList<>();

        if (timeTableDTO.getTimeTablePeriods() != null && !timeTableDTO.getTimeTablePeriods().isEmpty()) {
            for (TimeTablePeriodDTO timeTablePeriodDTO : timeTableDTO.getTimeTablePeriods()) {
                TimeTablePeriodEntity timeTablePeriodEntity = modelMapper.map(timeTablePeriodDTO, TimeTablePeriodEntity.class);
                timeTablePeriodEntity.setTimeTableEntity(timeTableEntity);

                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodDTO.getSubjectId())
                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodDTO.getTeacherId())
                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                timeTablePeriodEntity.setSubjectMasterEntity(subjectMasterEntity);
                timeTablePeriodEntity.setTeacher(employeeDetailsEntity);
                timeTablePeriodEntities.add(timeTablePeriodEntity);

            }
        }
        timeTableEntity.setTimeTablePeriodEntities(timeTablePeriodEntities);

        // saving timeTableEntity
        TimeTableEntity timeTableEntity1 = timeTableRepository.save(timeTableEntity);

        //  timeTableEntity to  TimeTableDTO
        TimeTableDTO tableDTO = modelMapper.map(timeTableEntity1, TimeTableDTO.class);
        List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();

        if (timeTableEntity1.getTimeTablePeriodEntities() != null && !timeTableEntity1.getTimeTablePeriodEntities().isEmpty()) {
            for (TimeTablePeriodEntity timeTablePeriodEntity : timeTableEntity1.getTimeTablePeriodEntities()) {
                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);
                timeTablePeriodDTO.setTimeTableId(tableDTO.getTimeTableId());
                timeTablePeriodDTOS.add(timeTablePeriodDTO);
            }
        }
        tableDTO.setTimeTablePeriods(timeTablePeriodDTOS);

        log.info("Exit from saveTimeTable");
        return tableDTO;
    }

    @Override
    public TimeTableDTO getTimeTableByTableId(Long tableId) {
        log.info("Enter into getTimeTableByTableId");

        TimeTableEntity timeTableEntity = timeTableRepository.findById(tableId)
                .orElseThrow(() -> new CustomException("Table not found", HttpStatus.NOT_FOUND));

        TimeTableDTO tableDTO = modelMapper.map(timeTableEntity, TimeTableDTO.class);

        tableDTO.setTimeTableId(timeTableEntity.getTimeTableId());
        tableDTO.setAcademicYear(timeTableEntity.getAcademicYear());
        tableDTO.setDivision(timeTableEntity.getDivision());

        if (timeTableEntity.getClassMasterEntity() != null) {
            tableDTO.setClassMasterId(timeTableEntity.getClassMasterEntity().getClassMasterId());
        }

        log.info("Exit from getTimeTableByTableId");
        return tableDTO;
    }

    @Override
    public TimeTableDTO updateTimeTable(TimeTableDTO timeTableDTO) {
        return null;
    }

    @Override
    public String deleteTimeTable(Long tableId) {
        log.info("Enter into deleteTimeTable");

        TimeTableEntity timeTableEntity = timeTableRepository.findById(tableId)
                .orElseThrow(() -> new CustomException("Student result not found", HttpStatus.NOT_FOUND));

        timeTableRepository.delete(timeTableEntity);
        log.info("Exit from deleteTimeTable");

        return "Record deleted successfully";


    }

    @Override
    public Map<String, Object> getALlTimeTableByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
