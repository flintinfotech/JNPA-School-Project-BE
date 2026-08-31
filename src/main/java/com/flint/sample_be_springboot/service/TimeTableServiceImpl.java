package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.EmployeeDetailsDTO;
import com.flint.sample_be_springboot.dto.SubjectMasterDTO;
import com.flint.sample_be_springboot.dto.TimeTableDTO;
import com.flint.sample_be_springboot.dto.TimeTablePeriodDTO;
import com.flint.sample_be_springboot.entity.EmployeeDetailsEntity;
import com.flint.sample_be_springboot.entity.SubjectMasterEntity;
import com.flint.sample_be_springboot.entity.TimeTableEntity;
import com.flint.sample_be_springboot.entity.TimeTablePeriodEntity;
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
        // 1. Validate request

        if (timeTableDTO == null) {
            throw new CustomException("Time table data can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        // 2. Check whether timetable already exists

        TimeTableEntity existingTimeTableEntity = timeTableRepository
                .findByStandardAndDivisionAndMediumAndAcademicYear(
                        timeTableDTO.getStandard(),
                        timeTableDTO.getDivision(),
                        timeTableDTO.getMedium(),
                        timeTableDTO.getAcademicYear()
                );

        // 3. If timetable already exists
        //    First check time conflict

        if (existingTimeTableEntity != null) {

            validateNewPeriodsAgainstExistingPeriods(timeTableDTO.getTimeTablePeriods(), existingTimeTableEntity.getTimeTablePeriodEntities());

            // If there is no time conflict
            // then timetable already exists
            throw new CustomException("Timetable for this class already exists", HttpStatus.CONFLICT);
        }

        // 4. Validate periods

        if (timeTableDTO.getTimeTablePeriods() == null || timeTableDTO.getTimeTablePeriods().isEmpty()) {
            throw new CustomException("At least one timetable period is required", HttpStatus.BAD_REQUEST);
        }

        // 5. Create TimeTableEntity
        TimeTableEntity timeTableEntity = modelMapper.map(timeTableDTO, TimeTableEntity.class);

        timeTableEntity.setAuditDetails(addAuditDetails(timeTableEntity.getAuditDetails()));

        List<TimeTablePeriodEntity> timeTablePeriodEntities = new ArrayList<>();

        // 6. Create TimeTablePeriodEntity

        for (TimeTablePeriodDTO periodDTO : timeTableDTO.getTimeTablePeriods()) {
            //Validate Day
            if (periodDTO.getDay() == null) {
                throw new CustomException("Day can't be null", HttpStatus.BAD_REQUEST);
            }

            // Validate Period Number

            if (periodDTO.getPeriodNumber() == null) {
                throw new CustomException("Period number can't be null", HttpStatus.BAD_REQUEST);
            }

            // Validate Start and End Time

            if (periodDTO.getStartTime() == null || periodDTO.getEndTime() == null) {

                throw new CustomException("Start time and end time can't be null", HttpStatus.BAD_REQUEST);
            }

            // Start time must be before end time
            if (!periodDTO.getStartTime()
                    .isBefore(periodDTO.getEndTime())) {

                throw new CustomException("Start time must be before end time", HttpStatus.BAD_REQUEST);
            }

            // Create period entity

            TimeTablePeriodEntity periodEntity = modelMapper.map(periodDTO, TimeTablePeriodEntity.class);

            // Set Parent

            periodEntity.setTimeTableEntity(timeTableEntity);

            // Set Subject

            if (periodDTO.getSubjectId() == null) {

                throw new CustomException("Subject can't be null", HttpStatus.BAD_REQUEST);
            }

            SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(periodDTO.getSubjectId())
                    .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

            periodEntity.setSubjectMasterEntity(subjectMasterEntity);

            // Set Teacher

            if (periodDTO.getEmployeeDetailsId() == null) {

                throw new CustomException("Teacher can't be null", HttpStatus.BAD_REQUEST);
            }

            EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(periodDTO.getEmployeeDetailsId())
                    .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

            periodEntity.setEmployeeDetailsEntity(employeeDetailsEntity);

            // Set Audit Details

            periodEntity.setAuditDetails(addAuditDetails(periodEntity.getAuditDetails()));

            // Add Period

            timeTablePeriodEntities.add(periodEntity);
        }

        // 7. Set periods to timetable

        timeTableEntity.setTimeTablePeriodEntities(timeTablePeriodEntities);

        // Check overlap between periods in same request

        validateTimeTablePeriodOverlaps(timeTablePeriodEntities);

        // 9. Save only after all validation succeeds

        TimeTableEntity savedTimeTableEntity = timeTableRepository.save(timeTableEntity);

        // 10. Convert TimeTable Entity to DTO

        TimeTableDTO tableDTO = modelMapper.map(savedTimeTableEntity, TimeTableDTO.class);

        List<TimeTablePeriodDTO> timeTablePeriodDTOS = new ArrayList<>();

        // 11. Convert Period Entity to DTO

        if (savedTimeTableEntity.getTimeTablePeriodEntities() != null && !savedTimeTableEntity.getTimeTablePeriodEntities().isEmpty()) {

            for (TimeTablePeriodEntity periodEntity : savedTimeTableEntity.getTimeTablePeriodEntities()) {

                TimeTablePeriodDTO periodDTO = modelMapper.map(periodEntity, TimeTablePeriodDTO.class);

                // Set TimeTable ID

                periodDTO.setTimeTableId(savedTimeTableEntity.getTimeTableId());

                // Set Subject DTO

                if (periodEntity.getSubjectMasterEntity() != null) {

                    SubjectMasterDTO subjectMasterDTO = modelMapper.map(periodEntity.getSubjectMasterEntity(), SubjectMasterDTO.class);

                    periodDTO.setSubjectMasterDTO(subjectMasterDTO);
                    periodDTO.setSubjectId(periodEntity.getSubjectMasterEntity().getSubjectMasterId());
                }

                // Set Teacher DTO

                if (periodEntity.getEmployeeDetailsEntity() != null) {

                    EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(periodEntity.getEmployeeDetailsEntity(), EmployeeDetailsDTO.class);

                    periodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);
                    periodDTO.setEmployeeDetailsId(periodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId());
                }

                // Add period DTO
                timeTablePeriodDTOS.add(periodDTO);
            }
        }

        // 12. Set periods in response

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
                if (timeTablePeriodEntity.getSubjectMasterEntity() != null) {
                    SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId())
                            .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                    SubjectMasterDTO subjectMasterDTO = modelMapper.map(subjectMasterEntity, SubjectMasterDTO.class);
                    timeTablePeriodDTO.setSubjectMasterDTO(subjectMasterDTO);
                }

                // set employee details dto
                if (timeTablePeriodEntity.getEmployeeDetailsEntity() != null) {
                    EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId())
                            .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                    EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(employeeDetailsEntity, EmployeeDetailsDTO.class);
                    timeTablePeriodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);
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
            throw new CustomException("Time table data can't be null", HttpStatus.PRECONDITION_FAILED);
        }

        if (timeTableDTO.getTimeTableId() == null) {
            throw new CustomException("Time table Id can't be null", HttpStatus.PRECONDITION_FAILED);
        }


        TimeTableEntity existingTimeTable = timeTableRepository.findById(timeTableDTO.getTimeTableId())
                .orElseThrow(() -> new CustomException("Time table not found", HttpStatus.NOT_FOUND));

        existingTimeTable.setDivision(timeTableDTO.getDivision());
        existingTimeTable.setStandard(timeTableDTO.getStandard());
        existingTimeTable.setMedium(timeTableDTO.getMedium());
        existingTimeTable.setAcademicYear(timeTableDTO.getAcademicYear());
        existingTimeTable.setAuditDetails(addAuditDetails(existingTimeTable.getAuditDetails()));

        if (timeTableDTO.getTimeTablePeriods() != null) {

            // Get all existing period IDs from request
            Set<Long> requestPeriodIds = timeTableDTO.getTimeTablePeriods()
                    .stream()
                    .map(TimeTablePeriodDTO::getTimeTablePeriodId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Remove periods which are not present in request
            existingTimeTable.getTimeTablePeriodEntities()
                    .removeIf(period -> period.getTimeTablePeriodId() != null
                            && !requestPeriodIds.contains(period.getTimeTablePeriodId()));

            // Create map of existing periods
            Map<Long, TimeTablePeriodEntity> existingPeriods = existingTimeTable.getTimeTablePeriodEntities()
                    .stream()
                    .filter(period -> period.getTimeTablePeriodId() != null)
                    .collect(Collectors.toMap(TimeTablePeriodEntity::getTimeTablePeriodId, Function.identity()));

            for (TimeTablePeriodDTO periodDTO : timeTableDTO.getTimeTablePeriods()) {

                // Validate period DTO
                if (periodDTO.getDay() == null) {
                    throw new CustomException("Day can't be null", HttpStatus.BAD_REQUEST);
                }

                if (periodDTO.getPeriodNumber() == null) {
                    throw new CustomException("Period number can't be null", HttpStatus.BAD_REQUEST);
                }

                if (periodDTO.getStartTime() == null || periodDTO.getEndTime() == null) {

                    throw new CustomException("Start time and end time can't be null", HttpStatus.BAD_REQUEST);
                }

                // Start time must be before end time
                if (!periodDTO.getStartTime().isBefore(periodDTO.getEndTime())) {

                    throw new CustomException("Start time must be before end time", HttpStatus.BAD_REQUEST);
                }

                TimeTablePeriodEntity periodEntity;

                if (periodDTO.getTimeTablePeriodId() != null && existingPeriods.containsKey(periodDTO.getTimeTablePeriodId())) {

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

//                //  Set Subject
//                if (periodDTO.getSubjectId() == null) {
//                    throw new CustomException("Subject can't be null", HttpStatus.BAD_REQUEST);
//                }
//
//                SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(periodDTO.getSubjectId())
//                        .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));
//
//                periodEntity.setSubjectMasterEntity(subjectMasterEntity);
//
//                // Set Teacher
//                if (periodDTO.getEmployeeDetailsId() == null) {
//                    throw new CustomException("Teacher can't be null", HttpStatus.BAD_REQUEST);
//                }
//
//                EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(periodDTO.getEmployeeDetailsId())
//                        .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));
//
//                periodEntity.setEmployeeDetailsEntity(employeeDetailsEntity);

                periodEntity.setTimeTableEntity(existingTimeTable);
            }

        } else {

            // If request contains null periods,
            // remove all existing periods
            existingTimeTable.getTimeTablePeriodEntities().clear();
        }
        // 8. Validate overlapping periods
        // BEFORE saving

        validateTimeTablePeriodOverlaps(existingTimeTable.getTimeTablePeriodEntities());

        TimeTableEntity updatedTimeTableEntity = timeTableRepository.save(existingTimeTable);

        TimeTableDTO updatedDTO = modelMapper.map(updatedTimeTableEntity, TimeTableDTO.class);

        List<TimeTablePeriodDTO> periodDTOS = new ArrayList<>();

        if (updatedTimeTableEntity.getTimeTablePeriodEntities() != null
                && !updatedTimeTableEntity.getTimeTablePeriodEntities().isEmpty()) {

            for (TimeTablePeriodEntity periodEntity : updatedTimeTableEntity.getTimeTablePeriodEntities()) {

                TimeTablePeriodDTO periodDTO = modelMapper.map(periodEntity, TimeTablePeriodDTO.class);

                // Set TimeTable ID
                periodDTO.setTimeTableId(updatedTimeTableEntity.getTimeTableId());

                // Set Subject DTO

                if (periodEntity.getSubjectMasterEntity() != null) {

                    SubjectMasterDTO subjectMasterDTO = modelMapper.map(periodEntity.getSubjectMasterEntity(), SubjectMasterDTO.class);
                    periodDTO.setSubjectMasterDTO(subjectMasterDTO);
                    periodDTO.setSubjectId(periodEntity.getSubjectMasterEntity().getSubjectMasterId());
                }

                // Set Teacher DTO

                if (periodEntity.getEmployeeDetailsEntity() != null) {

                    EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(periodEntity.getEmployeeDetailsEntity(), EmployeeDetailsDTO.class);
                    periodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);
                    periodDTO.setEmployeeDetailsId(periodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId());
                }
                periodDTOS.add(periodDTO);
            }
        }

        // Set periods in parent DTO
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
        log.info("Enter into getAllTimeTableByFilter");

        Page<TimeTableEntity> timeTableEntityPage;
        List<TimeTableEntity> timeTableEntities;
        long totalElement;

        String start = String.valueOf(getStartDate().getYear());
        String end = String.valueOf(getEndDate().getYear());

        String academicYear = start.concat("-").concat(end);

        System.err.println(academicYear);

        filter.put("academicYear", academicYear);

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
                if (timeTablePeriodEntity.getSubjectMasterEntity() != null) {
                    SubjectMasterEntity subjectMasterEntity = subjectMasterRepository.findById(timeTablePeriodEntity.getSubjectMasterEntity().getSubjectMasterId())
                            .orElseThrow(() -> new CustomException("Subject not found", HttpStatus.NOT_FOUND));

                    SubjectMasterDTO subjectMasterDTO = modelMapper.map(subjectMasterEntity, SubjectMasterDTO.class);
                    timeTablePeriodDTO.setSubjectMasterDTO(subjectMasterDTO);
                }

                // set employee details dto
                if (timeTablePeriodEntity.getEmployeeDetailsEntity() != null) {
                    EmployeeDetailsEntity employeeDetailsEntity = employeeDetailsRepository.findById(timeTablePeriodEntity.getEmployeeDetailsEntity().getEmployeeDetailsId())
                            .orElseThrow(() -> new CustomException("Teacher not found", HttpStatus.NOT_FOUND));

                    EmployeeDetailsDTO employeeDetailsDTO = modelMapper.map(employeeDetailsEntity, EmployeeDetailsDTO.class);
                    timeTablePeriodDTO.setEmployeeDetailsDTO(employeeDetailsDTO);
                }

                timeTablePeriodDTOS.add(timeTablePeriodDTO);
            }
            timeTableDTO.setTimeTablePeriods(timeTablePeriodDTOS);
            timeTableDTOS.add(timeTableDTO);
        }
        log.info("Exit from getAllTimeTableByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("Time TableDTOS", timeTableDTOS);
        map.put("Total Elements", totalElement);
        return map;
    }


    // HELPER METHOD
    private void validateTimeTablePeriodOverlaps(List<TimeTablePeriodEntity> periods) {

        if (periods == null || periods.isEmpty()) {
            return;
        }

        for (int i = 0; i < periods.size(); i++) {

            TimeTablePeriodEntity firstPeriod = periods.get(i);
            for (int j = i + 1; j < periods.size(); j++) {

                TimeTablePeriodEntity secondPeriod = periods.get(j);

                // Different days are allowed
                if (!firstPeriod.getDay().equals(secondPeriod.getDay())) {
                    continue;
                }

                // Check time overlap
                boolean isOverlapping = firstPeriod.getStartTime().isBefore(secondPeriod.getEndTime())
                        && firstPeriod.getEndTime().isAfter(secondPeriod.getStartTime());

                if (isOverlapping) {
                    throw new CustomException("Other Period is exist for this time "
                            + firstPeriod.getDay()
                            + ". Time "
                            + firstPeriod.getStartTime()
                            + " - "
                            + firstPeriod.getEndTime()
                            + " conflicts with "
                            + secondPeriod.getStartTime()
                            + " - "
                            + secondPeriod.getEndTime(),
                            HttpStatus.CONFLICT
                    );
                }
            }
        }
    }

    private void validateNewPeriodsAgainstExistingPeriods(
            List<TimeTablePeriodDTO> newPeriods,
            List<TimeTablePeriodEntity> existingPeriods) {

        if (newPeriods == null || newPeriods.isEmpty()) {
            return;
        }

        if (existingPeriods == null || existingPeriods.isEmpty()) {
            return;
        }

        for (TimeTablePeriodDTO newPeriod : newPeriods) {

            if (newPeriod.getDay() == null) {
                throw new CustomException("Day can't be null", HttpStatus.BAD_REQUEST);
            }

            if (newPeriod.getStartTime() == null || newPeriod.getEndTime() == null) {

                throw new CustomException("Start time and end time can't be null", HttpStatus.BAD_REQUEST);
            }

            if (!newPeriod.getStartTime().isBefore(newPeriod.getEndTime())) {

                throw new CustomException("Start time must be before end time", HttpStatus.BAD_REQUEST);
            }

            for (TimeTablePeriodEntity existingPeriod : existingPeriods) {

                // Different day → no conflict
                if (!newPeriod.getDay().equals(existingPeriod.getDay())) {
                    continue;
                }

                // Check overlap
                boolean isOverlapping = newPeriod.getStartTime().isBefore(existingPeriod.getEndTime())
                        && newPeriod.getEndTime().isAfter(existingPeriod.getStartTime());

                if (isOverlapping) {

                    throw new CustomException(
                            "Time conflict found on "
                                    + newPeriod.getDay()
                                    + ". Requested time "
                                    + newPeriod.getStartTime()
                                    + " - "
                                    + newPeriod.getEndTime()
                                    + " overlaps with existing period "
                                    + existingPeriod.getStartTime()
                                    + " - "
                                    + existingPeriod.getEndTime(),
                            HttpStatus.CONFLICT
                    );
                }
            }
        }

        // Also check if two NEW periods overlap with each other
        for (int i = 0; i < newPeriods.size(); i++) {

            TimeTablePeriodDTO firstPeriod = newPeriods.get(i);

            for (int j = i + 1; j < newPeriods.size(); j++) {

                TimeTablePeriodDTO secondPeriod = newPeriods.get(j);

                if (!firstPeriod.getDay().equals(secondPeriod.getDay())) {
                    continue;
                }

                boolean isOverlapping = firstPeriod.getStartTime().isBefore(secondPeriod.getEndTime())
                        && firstPeriod.getEndTime().isAfter(secondPeriod.getStartTime());

                if (isOverlapping) {

                    throw new CustomException(
                            "Time conflict found on "
                                    + firstPeriod.getDay()
                                    + ". Period "
                                    + firstPeriod.getStartTime()
                                    + " - "
                                    + firstPeriod.getEndTime()
                                    + " overlaps with period "
                                    + secondPeriod.getStartTime()
                                    + " - "
                                    + secondPeriod.getEndTime(),
                            HttpStatus.CONFLICT
                    );
                }
            }
        }
    }
}
