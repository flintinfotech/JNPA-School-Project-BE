package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.TimeTableDTO;
import com.flint.sample_be_springboot.dto.TimeTablePeriodDTO;
import com.flint.sample_be_springboot.entity.*;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.*;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private UserRepository userRepository;

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
                timeTablePeriodEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));
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
    public TimeTableDTO getTimeTableByTimeTableId(Long timeTableId) {
        log.info("Enter into getTimeTableByTimeTableId");

        TimeTableEntity timeTableEntity = timeTableRepository.findById(timeTableId)
                .orElseThrow(() -> new CustomException("Table not found", HttpStatus.NOT_FOUND));

        TimeTableDTO tableDTO = modelMapper.map(timeTableEntity, TimeTableDTO.class);

        tableDTO.setTimeTableId(timeTableEntity.getTimeTableId());
        tableDTO.setAcademicYear(timeTableEntity.getAcademicYear());
        tableDTO.setDivision(timeTableEntity.getDivision());

        if (timeTableEntity.getClassMasterEntity() != null) {
            tableDTO.setClassMasterId(timeTableEntity.getClassMasterEntity().getClassMasterId());
        }

        List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();
        if (timeTableEntity.getTimeTablePeriodEntities() != null && !timeTableEntity.getTimeTablePeriodEntities().isEmpty()) {
            for (TimeTablePeriodEntity timeTablePeriodEntity : timeTableEntity.getTimeTablePeriodEntities()) {
                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);

                if (timeTablePeriodEntity.getSubjectMasterEntity() != null) {

                    timeTablePeriodDTO.setSubjectId(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId());
                }
                timeTablePeriodDTOS.add(timeTablePeriodDTO);
            }
        }

        tableDTO.setTimeTablePeriods(timeTablePeriodDTOS);
        log.info("Exit from getTimeTableByTimeTableId");

        return tableDTO;
    }

    @Override
    public TimeTableDTO updateTimeTable(TimeTableDTO timeTableDTO) {
        log.info("Enter into updateTimeTable");

        if (timeTableDTO == null) {
            throw new CustomException("Table table data can't null", HttpStatus.NOT_FOUND);
        }

        if (timeTableDTO.getTimeTableId() == null) {
            throw new CustomException("Time table Id can't be null", HttpStatus.NOT_FOUND);
        }

        TimeTableEntity timeTableEntity = timeTableRepository.findById(timeTableDTO.getTimeTableId())
                .orElseThrow(() -> new CustomException("Table not found", HttpStatus.NOT_FOUND));

        //Updating parent fields
        timeTableEntity.setDivision(timeTableDTO.getDivision());
        timeTableEntity.setAcademicYear(timeTableDTO.getAcademicYear());
        timeTableEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));

        //Update class master
        if (timeTableDTO.getClassMasterId() != null) {

            ClassMasterEntity classMasterEntity = classMasterRepository.findById(timeTableDTO.getClassMasterId())
                    .orElseThrow(() -> new CustomException("Class not found", HttpStatus.NOT_FOUND));

            timeTableEntity.setClassMasterEntity(classMasterEntity);
            timeTableEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));
        }

        // Clear old periods
        timeTableEntity.getTimeTablePeriodEntities().clear();

        if (timeTableEntity.getTimeTablePeriodEntities() != null && !timeTableDTO.getTimeTablePeriods().isEmpty()) {
            for (TimeTablePeriodDTO timeTablePeriodDTO : timeTableDTO.getTimeTablePeriods()) {

                TimeTablePeriodEntity timeTablePeriodEntity = new TimeTablePeriodEntity();

                timeTablePeriodEntity.setDay(timeTablePeriodDTO.getDay());
                timeTablePeriodEntity.setPeriodNumber(timeTablePeriodDTO.getPeriodNumber());
                timeTablePeriodEntity.setStartTime(timeTablePeriodDTO.getStartTime());
                timeTablePeriodEntity.setEndTime(timeTablePeriodDTO.getEndTime());
                timeTablePeriodEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));

                // Set subject
                if (timeTablePeriodDTO.getSubjectId() != null) {

                    SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodDTO.getSubjectId())
                            .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                    timeTablePeriodEntity.setSubjectMasterEntity(subjectMasterEntity);

                    // Teacher mapping
                    EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodDTO.getTeacherId())
                            .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                    timeTablePeriodEntity.setTeacher(employeeDetailsEntity);
                }

                timeTablePeriodEntity.setTimeTableEntity(timeTableEntity);
                timeTableEntity.getTimeTablePeriodEntities().add(timeTablePeriodEntity);
            }
        }

        TimeTableEntity updatedTimeTableEntity = timeTableRepository.save(timeTableEntity);

        TimeTableDTO tableDTO = modelMapper.map(updatedTimeTableEntity, TimeTableDTO.class);
        List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();

        if (updatedTimeTableEntity.getTimeTablePeriodEntities() != null && !updatedTimeTableEntity.getTimeTablePeriodEntities().isEmpty()) {

            for (TimeTablePeriodEntity timeTablePeriodEntity : updatedTimeTableEntity.getTimeTablePeriodEntities()) {

                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);
                timeTablePeriodDTOS.add(timeTablePeriodDTO);
            }
        }
        tableDTO.setTimeTablePeriods(timeTablePeriodDTOS);


        log.info("Exit from updateTimeTable");
        return tableDTO;
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
        log.info("Enter into getALlTimeTableByFilter");

        Page<TimeTableEntity> timeTableEntityPage;
        List<TimeTableEntity> timeTableEntities;

        long totalElement;

        CustomQuerySpecification<TimeTableEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            timeTableEntityPage = timeTableRepository.findAll(customQuerySpecification, pageable);
            timeTableEntities = timeTableEntityPage.getContent();
            totalElement = timeTableEntityPage.getTotalElements();
        } else {
            timeTableEntities = timeTableRepository.findAll(customQuerySpecification);
            totalElement = timeTableEntities.size();
        }

        List<TimeTableDTO> timeTableDTOS = new ArrayList<>();

        for (TimeTableEntity timeTableEntity : timeTableEntities) {
            TimeTableDTO timeTableDTO = modelMapper.map(timeTableEntity, TimeTableDTO.class);
            timeTableDTO.setTimeTableId(timeTableEntity.getTimeTableId());

            if (timeTableEntity.getClassMasterEntity() != null) {
                timeTableDTO.setClassMasterId(timeTableEntity.getClassMasterEntity().getClassMasterId());
            }

            List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();
            for (TimeTablePeriodEntity timeTablePeriodEntity : timeTableEntity.getTimeTablePeriodEntities()) {
                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);
                timeTablePeriodDTO.setTimeTablePeriodId(timeTablePeriodEntity.getTimeTablePeriodId());
                timeTablePeriodDTOS.add(timeTablePeriodDTO);
            }
            timeTableDTO.setTimeTablePeriods(timeTablePeriodDTOS);
            timeTableDTOS.add(timeTableDTO);
        }
        log.info("Exit from getALlTimeTableByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("Time TableDTOS", timeTableDTOS);
        map.put("Total Elements", totalElement);
        return map;
    }
}
