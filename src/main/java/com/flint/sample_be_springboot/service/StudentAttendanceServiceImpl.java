package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.StudentAttendanceDTO;
import com.flint.sample_be_springboot.entity.student.StudentAttendanceEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.notification.NotificationService;
import com.flint.sample_be_springboot.repository.student.StudentAttendanceRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
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
public class StudentAttendanceServiceImpl extends BaseService implements StudentAttendanceService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private StudentAttendanceRepository studentAttendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public StudentAttendanceDTO getStudentAttendanceById(Long studentAttendanceId) {
        log.info("Enter into getStudentAttendanceById");

        StudentAttendanceEntity studentAttendanceEntity = studentAttendanceRepository.findById(studentAttendanceId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        StudentAttendanceDTO studentAttendanceDTO = modelMapper.map(studentAttendanceEntity, StudentAttendanceDTO.class);
        studentAttendanceDTO.setStudentId(studentAttendanceEntity.getStudentEntity().getStudentId());

        log.info("Exit from getStudentAttendanceById");
        return studentAttendanceDTO;
    }

    @Override
    public StudentAttendanceDTO saveStudentAttendance(StudentAttendanceDTO studentAttendanceDTO) {
        log.info("Enter into saveStudentAttendance");

        if (studentAttendanceDTO == null) {
            throw new CustomException("studentAttendanceDTO should be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentAttendanceEntity isPresent = studentAttendanceRepository.findByAttendanceDateAndAcademicYearAndStudentEntity_StudentId
                (studentAttendanceDTO.getAttendanceDate(), studentAttendanceDTO.getStudentId(), studentAttendanceDTO.getAcademicYear());

        if (isPresent != null) {
            throw new CustomException("Attendance of this student is already marked", HttpStatus.CONFLICT);
        }

        String startYear = String.valueOf(getStartDate().getYear());
        String endYear = String.valueOf(getEndDate().getYear());

        String academicYear = startYear.concat("-").concat(endYear);

        StudentEntity studentEntity = studentRepository.findById(studentAttendanceDTO.getStudentId())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentAttendanceEntity studentAttendanceEntity = modelMapper.map(studentAttendanceDTO, StudentAttendanceEntity.class);
        studentAttendanceEntity.setAcademicYear(academicYear);
        studentAttendanceEntity.setStudentEntity(studentEntity);
        studentAttendanceEntity.setAuditDetails(addAuditDetails(studentAttendanceEntity.getAuditDetails()));

        StudentAttendanceEntity savedEntity = studentAttendanceRepository.save(studentAttendanceEntity);

        // send notification on parent mobile
//        notificationService.sendNotification(studentEntity.getParentEntity().getPhone(),
//                studentAttendanceDTO.getAttendanceStatus().toString());

        StudentAttendanceDTO savedData = modelMapper.map(savedEntity, StudentAttendanceDTO.class);
        savedData.setStudentId(studentAttendanceEntity.getStudentEntity().getStudentId());

        log.info("Exit from saveStudentAttendance");
        return savedData;
    }

    @Override
    public StudentAttendanceDTO updateStudentAttendance(StudentAttendanceDTO studentAttendanceDTO) {
        log.info("Enter into updateStudentAttendance");

        if (studentAttendanceDTO == null) {
            throw new CustomException("studentAttendanceDTO should be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentAttendanceEntity existingStudentAttendanceEntity = studentAttendanceRepository.findById(studentAttendanceDTO.getStudentAttendanceId())
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        StudentEntity studentEntity = studentRepository.findById(studentAttendanceDTO.getStudentId())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        existingStudentAttendanceEntity.setAttendanceStatus(studentAttendanceDTO.getAttendanceStatus());
        existingStudentAttendanceEntity.setAttendanceDate(studentAttendanceDTO.getAttendanceDate());
        existingStudentAttendanceEntity.setAcademicYear(studentAttendanceDTO.getAcademicYear());
        existingStudentAttendanceEntity.setStudentEntity(studentEntity);
        existingStudentAttendanceEntity.setAuditDetails(addAuditDetails(existingStudentAttendanceEntity.getAuditDetails()));

        StudentAttendanceEntity updatedEntity = studentAttendanceRepository.save(existingStudentAttendanceEntity);

        // send notification on parent mobile
//        notificationService.sendNotification(studentEntity.getParentEntity().getPhone(),
//                studentAttendanceDTO.getAttendanceStatus().toString());

        StudentAttendanceDTO updatedData = modelMapper.map(updatedEntity, StudentAttendanceDTO.class);
        updatedData.setStudentId(existingStudentAttendanceEntity.getStudentEntity().getStudentId());

        log.info("Exit from updateStudentAttendance");

        return updatedData;
    }

    @Override
    public String deleteStudentAttendance(Long studentAttendanceId) {
        log.info("Enter into deleteStudentAttendance");

        StudentAttendanceEntity studentAttendanceEntity = studentAttendanceRepository.findById(studentAttendanceId)
                .orElseThrow(() -> new CustomException("Record not found", HttpStatus.NOT_FOUND));

        studentAttendanceRepository.delete(studentAttendanceEntity);

        log.info("Exit from deleteStudentAttendance");

        return "Record delete successfully";
    }

    @Override
    public Map<String, Object> getAllStudentAttendanceByFilter(Map<String, Object> filterBody, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllStudentAttendanceByFilter");

        Page<StudentAttendanceEntity> studentAttendanceEntityPage;
        List<StudentAttendanceEntity> studentAttendanceEntities;
        long totalElement;

        String startYear = String.valueOf(getStartDate().getYear());
        String endYear = String.valueOf(getEndDate().getYear());

        String academicYear = startYear.concat("-").concat(endYear);

        filterBody.put("academicYear", academicYear);

        if(filterBody.containsKey("studentId")){
            filterBody.put("studentEntity.studentId", filterBody.get("studentId"));
            filterBody.remove("studentId");
        }

        CustomQuerySpecification<StudentAttendanceEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filterBody);

        if (paginate) {
            studentAttendanceEntityPage = studentAttendanceRepository.findAll(customQuerySpecification, pageable);
            studentAttendanceEntities = studentAttendanceEntityPage.getContent();
            totalElement = studentAttendanceEntityPage.getTotalElements();
        } else {
            studentAttendanceEntities = studentAttendanceRepository.findAll(customQuerySpecification);
            totalElement = studentAttendanceEntities.size();
        }

        List<StudentAttendanceDTO> studentAttendanceDTOS = studentAttendanceEntities.stream()
                .map(sa -> {
                    StudentAttendanceDTO dto = modelMapper.map(sa, StudentAttendanceDTO.class);
                    dto.setStudentId(sa.getStudentEntity().getStudentId());

                    return dto;
                }).collect(Collectors.toUnmodifiableList());

        log.info("Exit from getAllStudentAttendanceByFilter");

        Map<String, Object> map = new HashMap<>();
        map.put("Data", studentAttendanceDTOS);
        map.put("total elements", totalElement);
        return map;
    }
}
