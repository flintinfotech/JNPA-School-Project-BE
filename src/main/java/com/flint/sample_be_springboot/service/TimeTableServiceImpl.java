package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeDetailsDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        timeTableEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));

        List<TimeTablePeriodEntity> timeTablePeriodEntities = new ArrayList<>();

        if (timeTableDTO.getTimeTablePeriods() != null && !timeTableDTO.getTimeTablePeriods().isEmpty()) {
            for (TimeTablePeriodDTO timeTablePeriodDTO : timeTableDTO.getTimeTablePeriods()) {
                TimeTablePeriodEntity timeTablePeriodEntity = modelMapper.map(timeTablePeriodDTO, TimeTablePeriodEntity.class);
                timeTablePeriodEntity.setTimeTableEntity(timeTableEntity);

                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodDTO.getSubjectId())
                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodDTO.getEmployeeDetailsId())
                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                timeTablePeriodEntity.setSubjectMasterEntity(subjectMasterEntity);
                timeTablePeriodEntity.setEmployeeDetailsEntity(employeeDetailsEntity);
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

                // set subject master dto
                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId())
                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                SubjectMasterDTO subjectMasterDTO = modelMapper.map(subjectMasterEntity, SubjectMasterDTO.class);
                timeTablePeriodDTO.setSubjectMasterDTO(subjectMasterDTO);

                // set employee details dto
                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId())
                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(employeeDetailsEntity, EmployeeDetailsDTO.class);
                timeTablePeriodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);

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

        List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();
        if (timeTableEntity.getTimeTablePeriodEntities() != null && !timeTableEntity.getTimeTablePeriodEntities().isEmpty()) {
            for (TimeTablePeriodEntity timeTablePeriodEntity : timeTableEntity.getTimeTablePeriodEntities()) {
                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);

                // set subject master dto
                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId())
                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                SubjectMasterDTO subjectMasterDTO = modelMapper.map(subjectMasterEntity, SubjectMasterDTO.class);
                timeTablePeriodDTO.setSubjectMasterDTO(subjectMasterDTO);

                // set employee details dto
                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId())
                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(employeeDetailsEntity, EmployeeDetailsDTO.class);
                timeTablePeriodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);

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
            throw new CustomException("Time table data can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (timeTableDTO.getTimeTableId() == null) {
            throw new CustomException("Time table Id can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        TimeTableEntity existingTimeTable = timeTableRepository.findById(timeTableDTO.getTimeTableId())
                .orElseThrow(() -> new CustomException("Table not found", HttpStatus.NOT_FOUND));

        // Update parent fields
        existingTimeTable.setDivision(timeTableDTO.getDivision());
        existingTimeTable.setAcademicYear(timeTableDTO.getAcademicYear());

        existingTimeTable.setAuditDetails(addAuditDetails(existingTimeTable.getAuditDetails()));

        // Update Time Table Periods
        if (timeTableDTO.getTimeTablePeriods() != null) {

            // IDs coming from request
            Set<Long> requestPeriodIds = timeTableDTO.getTimeTablePeriods().stream()
                    .map(TimeTablePeriodDTO::getTimeTablePeriodId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Remove periods which are not present in request
            existingTimeTable.getTimeTablePeriodEntities().removeIf(period ->
                    period.getTimeTablePeriodId() != null && !requestPeriodIds.contains(period.getTimeTablePeriodId()));

            // Existing period map
            Map<Long, TimeTablePeriodEntity> existingPeriods =
                    existingTimeTable.getTimeTablePeriodEntities().stream()
                            .filter(period -> period.getTimeTablePeriodId() != null)
                            .collect(Collectors.toMap(
                                    TimeTablePeriodEntity::getTimeTablePeriodId,
                                    Function.identity()
                            ));

            // Update / Add periods
            for (TimeTablePeriodDTO periodDTO : timeTableDTO.getTimeTablePeriods()) {

                TimeTablePeriodEntity periodEntity;

                // Update existing period
                if (periodDTO.getTimeTablePeriodId() != null &&
                        existingPeriods.containsKey(
                                periodDTO.getTimeTablePeriodId())) {

                    periodEntity = existingPeriods.get(periodDTO.getTimeTablePeriodId());

                    periodEntity.setDay(periodDTO.getDay());
                    periodEntity.setPeriodNumber(periodDTO.getPeriodNumber());
                    periodEntity.setStartTime(periodDTO.getStartTime());
                    periodEntity.setEndTime(periodDTO.getEndTime());

                    periodEntity.setAuditDetails(addAuditDetails(periodEntity.getAuditDetails()));

                }

                // Add new period
                else {

                    periodEntity = new TimeTablePeriodEntity();

                    periodEntity.setDay(periodDTO.getDay());
                    periodEntity.setPeriodNumber(periodDTO.getPeriodNumber());
                    periodEntity.setStartTime(periodDTO.getStartTime());
                    periodEntity.setEndTime(periodDTO.getEndTime());

                    periodEntity.setTimeTableEntity(existingTimeTable);

                    periodEntity.setAuditDetails(addAuditDetails(periodEntity.getAuditDetails()));

                    existingTimeTable.getTimeTablePeriodEntities().add(periodEntity);
                }

                // Set Subject
                if (periodDTO.getSubjectId() != null) {

                    SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(periodDTO.getSubjectId())
                            .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                    periodEntity.setSubjectMasterEntity(subjectMasterEntity);
                } else {
                    periodEntity.setSubjectMasterEntity(null);
                }

                // Set Teacher
                if (periodDTO.getEmployeeDetailsId() != null) {

                    EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(periodDTO.getEmployeeDetailsId())
                            .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                    periodEntity.setEmployeeDetailsEntity(employeeDetailsEntity);
                } else {
                    periodEntity.setEmployeeDetailsEntity(null);
                }

                // Make sure parent is always set
                periodEntity.setTimeTableEntity(existingTimeTable);
            }
        } else {

            // If request contains null periods,
            // remove all existing periods.
            existingTimeTable.getTimeTablePeriodEntities().clear();
        }

        // Save
        TimeTableEntity updatedTimeTableEntity =
                timeTableRepository.save(existingTimeTable);

        // Convert to DTO
        TimeTableDTO updatedDTO = modelMapper.map(updatedTimeTableEntity, TimeTableDTO.class);

        List<TimeTablePeriodDTO> periodDTOS = new ArrayList<>();

        if (updatedTimeTableEntity.getTimeTablePeriodEntities() != null &&
                !updatedTimeTableEntity.getTimeTablePeriodEntities().isEmpty()) {

            for (TimeTablePeriodEntity periodEntity : updatedTimeTableEntity.getTimeTablePeriodEntities()) {

                TimeTablePeriodDTO periodDTO = modelMapper.map(periodEntity, TimeTablePeriodDTO.class);

                // Set Subject DTO
                if (periodEntity.getSubjectMasterEntity() != null) {

                    SubjectMasterDTO subjectMasterDTO = modelMapper.map(periodEntity.getSubjectMasterEntity(), SubjectMasterDTO.class);

                    periodDTO.setSubjectMasterDTO(subjectMasterDTO);

                    periodDTO.setSubjectId(periodEntity.getSubjectMasterEntity().getSubjectMasterId());
                }

                // Set Teacher DTO
                if (periodEntity.getEmployeeDetailsEntity() != null) {

                    EmployeeDetailsDTO employeeDetailsDTO =
                            modelMapper.map(periodEntity.getEmployeeDetailsEntity(), EmployeeDetailsDTO.class);

                    periodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);

                    periodDTO.setEmployeeDetailsId(periodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId());
                }

                periodDTOS.add(periodDTO);
            }
        }

        updatedDTO.setTimeTablePeriods(periodDTOS);

        log.info("Exit from updateTimeTable");

        return updatedDTO;
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
    public Map<String, Object> getAllTimeTableByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
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

            List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();
            for (TimeTablePeriodEntity timeTablePeriodEntity : timeTableEntity.getTimeTablePeriodEntities()) {
                TimeTablePeriodDTO timeTablePeriodDTO = modelMapper.map(timeTablePeriodEntity, TimeTablePeriodDTO.class);

                // set subject master dto
                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId())
                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                SubjectMasterDTO subjectMasterDTO = modelMapper.map(subjectMasterEntity, SubjectMasterDTO.class);
                timeTablePeriodDTO.setSubjectMasterDTO(subjectMasterDTO);

                // set employee details dto
                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId())
                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(employeeDetailsEntity, EmployeeDetailsDTO.class);
                timeTablePeriodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);

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
