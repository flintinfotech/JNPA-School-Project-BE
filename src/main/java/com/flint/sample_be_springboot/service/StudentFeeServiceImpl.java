package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.student.FeePaymentDTO;
import com.flint.sample_be_springboot.dto.student.StudentFeeDTO;
import com.flint.sample_be_springboot.entity.student.FeePaymentEntity;
import com.flint.sample_be_springboot.entity.student.StudentEntity;
import com.flint.sample_be_springboot.entity.student.StudentFeeEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.student.FeePaymentRepository;
import com.flint.sample_be_springboot.repository.student.StudentFeeRepository;
import com.flint.sample_be_springboot.repository.student.StudentRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import com.flint.sample_be_springboot.util.GenerateCodes;
import com.flint.sample_be_springboot.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentFeeServiceImpl extends BaseService implements StudentFeeService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeePaymentRepository feePaymentRepository;

    @Override
    public StudentFeeDTO getStudentFee(Long studentFeeId) {
        log.info("Enter into getStudentFee");

        StudentFeeEntity studentFee = studentFeeRepository.findById(studentFeeId)
                .orElseThrow(() -> new CustomException("Student fee record not found", HttpStatus.NOT_FOUND));

        StudentFeeDTO studentResultDTO = modelMapper.map(studentFee, StudentFeeDTO.class);
        studentResultDTO.setStudentId(studentFee.getStudentEntity().getStudentId());


        List<FeePaymentDTO> feePaymentDTOList = new ArrayList<>();
        if (studentFee.getFeePaymentEntities() != null && !studentFee.getFeePaymentEntities().isEmpty()) {
            for (FeePaymentEntity feePaymentEntity : studentFee.getFeePaymentEntities()) {
                FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                feePaymentDTOList.add(feePaymentDTO);
            }
        }
        studentResultDTO.setFeePaymentDTOS(feePaymentDTOList);

        log.info("Exit from getStudentFee");
        return studentResultDTO;
    }

    @Override
    public StudentFeeDTO saveStudentFee(StudentFeeDTO studentFeeDTO) {
        log.info("Enter into saveStudentFee");

        if (studentFeeDTO == null) {
            throw new CustomException("Student fee record should not be null", HttpStatus.NOT_FOUND);
        }

        StudentEntity studentEntity = studentRepository.findById(studentFeeDTO.getStudentId())
                .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

        StudentFeeEntity studentFeeEntity = modelMapper.map(studentFeeDTO, StudentFeeEntity.class);
        studentFeeEntity.setStudentEntity(studentEntity);

        studentFeeEntity.setAuditDetails(addAuditDetails(studentFeeEntity.getAuditDetails()));

        List<FeePaymentEntity> feePaymentEntities = new ArrayList<>();
        if (studentFeeDTO.getFeePaymentDTOS() != null && !studentFeeDTO.getFeePaymentDTOS().isEmpty()) {

            // Get last generated receipt number
            String lastReceiptNo =feePaymentRepository.findLastReceiptNo();

            for (FeePaymentDTO feePaymentDTO : studentFeeDTO.getFeePaymentDTOS()) {
                FeePaymentEntity feePaymentEntity = modelMapper.map(feePaymentDTO, FeePaymentEntity.class);

                // Generate next receipt number
                String nextReceiptNo =GenerateCodes.generateReceiptNo(lastReceiptNo);

                // Set generated receipt number
                feePaymentEntity.setReceiptNo(nextReceiptNo);

                // Update lastReceiptNo so next iteration generates the next number
                lastReceiptNo = nextReceiptNo;

                feePaymentEntity.setAuditDetails(addAuditDetails(feePaymentEntity.getAuditDetails()));
                feePaymentEntity.setStudentFee(studentFeeEntity);
                feePaymentEntities.add(feePaymentEntity);
            }
        }
        studentFeeEntity.setFeePaymentEntities(feePaymentEntities);

        StudentFeeEntity savedStudentFeeEntity = studentFeeRepository.save(studentFeeEntity);
        StudentFeeDTO savedStudentFeeDTO = modelMapper.map(savedStudentFeeEntity, StudentFeeDTO.class);
        savedStudentFeeDTO.setStudentId(savedStudentFeeEntity.getStudentEntity().getStudentId());

        List<FeePaymentDTO> feePaymentDTOList = new ArrayList<>();
        if (savedStudentFeeEntity.getFeePaymentEntities() != null && !savedStudentFeeEntity.getFeePaymentEntities().isEmpty()) {
            for (FeePaymentEntity feePaymentEntity : savedStudentFeeEntity.getFeePaymentEntities()) {
                FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                feePaymentDTOList.add(feePaymentDTO);
            }
        }
        savedStudentFeeDTO.setFeePaymentDTOS(feePaymentDTOList);

        log.info("Exit from saveStudentFee");
        return savedStudentFeeDTO;
    }

    @Override
    public StudentFeeDTO updateStudentFee(StudentFeeDTO studentFeeDTO) {

        log.info("Enter into updateStudentFee");

        if (studentFeeDTO == null) {
            throw new CustomException("Student fee record should not be null", HttpStatus.PRECONDITION_FAILED);
        }

        StudentFeeEntity existingStudentFeeEntity = studentFeeRepository.findById(studentFeeDTO.getStudentFeeId())
                .orElseThrow(() -> new CustomException("Student fee record not found", HttpStatus.NOT_FOUND));

        // Set Student Entity
        if (studentFeeDTO.getStudentId() != null) {

            StudentEntity studentEntity = studentRepository.findById(studentFeeDTO.getStudentId())
                    .orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));

            existingStudentFeeEntity.setStudentEntity(studentEntity);
        }

        // Update basic fields
        existingStudentFeeEntity.setAcademicYear(studentFeeDTO.getAcademicYear());
        existingStudentFeeEntity.setFeeName(studentFeeDTO.getFeeName());
        existingStudentFeeEntity.setTotalFeeAmount(studentFeeDTO.getTotalFeeAmount());
        existingStudentFeeEntity.setPaidAmount(studentFeeDTO.getPaidAmount());
        existingStudentFeeEntity.setPendingAmount(studentFeeDTO.getPendingAmount());
        existingStudentFeeEntity.setDueAmount(studentFeeDTO.getDueAmount());



        existingStudentFeeEntity.setAuditDetails(addAuditDetails(existingStudentFeeEntity.getAuditDetails()));

        // Delete removed fee payments
        if (studentFeeDTO.getFeePaymentDTOS() != null) {

            Set<Long> requestPaymentIds = studentFeeDTO.getFeePaymentDTOS().stream()
                    .map(FeePaymentDTO::getFeePaymentId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            existingStudentFeeEntity.getFeePaymentEntities().removeIf(payment ->
                    payment.getFeePaymentId() != null && !requestPaymentIds.contains(payment.getFeePaymentId())
            );

            // Existing payment map
            Map<Long, FeePaymentEntity> existingPayments = existingStudentFeeEntity.getFeePaymentEntities()
                    .stream()
                    .filter(payment -> payment.getFeePaymentId() != null)
                    .collect(Collectors.toMap(
                            FeePaymentEntity::getFeePaymentId,
                            Function.identity()
                    ));

            // Update / Add payments
            for (FeePaymentDTO paymentDTO : studentFeeDTO.getFeePaymentDTOS()) {

                FeePaymentEntity feePaymentEntity;

                // Update existing payment
                if (paymentDTO.getFeePaymentId() != null && existingPayments.containsKey(paymentDTO.getFeePaymentId())) {

                    feePaymentEntity = existingPayments.get(paymentDTO.getFeePaymentId());

                    feePaymentEntity.setAmount(paymentDTO.getAmount());
                    feePaymentEntity.setPaymentMode(paymentDTO.getPaymentMode());
                    feePaymentEntity.setPaymentDate(paymentDTO.getPaymentDate());
                    feePaymentEntity.setTransactionId(paymentDTO.getTransactionId());
                    feePaymentEntity.setRemarks(paymentDTO.getRemarks());
                    feePaymentEntity.setReceiptNo(paymentDTO.getReceiptNo());
                    feePaymentEntity.setStatus(paymentDTO.getStatus());

                    feePaymentEntity.setAuditDetails(addAuditDetails(feePaymentEntity.getAuditDetails()));

                    feePaymentEntity.setStudentFee(existingStudentFeeEntity);

                } else {

                    // Add new payment
                    feePaymentEntity = modelMapper.map(paymentDTO, FeePaymentEntity.class);

                    // Get last generated receipt number
                    String lastReceiptNo =feePaymentRepository.findLastReceiptNo();

                    // Generate next receipt number
                    String nextReceiptNo =GenerateCodes.generateReceiptNo(lastReceiptNo);

                    // Set generated receipt number
                    feePaymentEntity.setReceiptNo(nextReceiptNo);
                    feePaymentEntity.setAuditDetails(addAuditDetails(feePaymentEntity.getAuditDetails()));

                    existingStudentFeeEntity.getFeePaymentEntities().add(feePaymentEntity);
                }
            }
        }

        // Save student fee
        StudentFeeEntity updatedStudentFeeEntity = studentFeeRepository.save(existingStudentFeeEntity);

        // Return updated DTO
        StudentFeeDTO updatedStudentFeeDTO = modelMapper.map(updatedStudentFeeEntity, StudentFeeDTO.class);

        if (updatedStudentFeeEntity.getStudentEntity() != null) {
            updatedStudentFeeDTO.setStudentId(updatedStudentFeeEntity.getStudentEntity().getStudentId());
        }

        // Set Fee Payment DTOs
        List<FeePaymentDTO> feePaymentDTOList = new ArrayList<>();

        if (updatedStudentFeeEntity.getFeePaymentEntities() != null && !updatedStudentFeeEntity.getFeePaymentEntities().isEmpty()) {

            for (FeePaymentEntity feePaymentEntity : updatedStudentFeeEntity.getFeePaymentEntities()) {

                FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                feePaymentDTO.setStudentFeeId(updatedStudentFeeEntity.getStudentFeeId());
                feePaymentDTO.setStatus(feePaymentDTO.getStatus());
                feePaymentDTOList.add(feePaymentDTO);
            }
        }

        updatedStudentFeeDTO.setFeePaymentDTOS(feePaymentDTOList);

        log.info("Exit from updateStudentFee");

        return updatedStudentFeeDTO;
    }

    @Override
    public String deleteStudentFeeById(Long studentFeeId) {
        log.info("Enter into getStudentFee");

        StudentFeeEntity studentFee = studentFeeRepository.findById(studentFeeId)
                .orElseThrow(() -> new CustomException("Student fee record not found", HttpStatus.NOT_FOUND));

        studentFeeRepository.delete(studentFee);

        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllStudentsFeeByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllStudentsFeeByFilter");

        Page<StudentFeeEntity> studentFeeEntityPage;
        List<StudentFeeEntity> studentFeeEntities;
        long totalElement;

        CustomQuerySpecification<StudentFeeEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            studentFeeEntityPage = studentFeeRepository.findAll(customQuerySpecification, pageable);
            studentFeeEntities = studentFeeEntityPage.getContent();
            totalElement = studentFeeEntityPage.getTotalElements();
        } else {
            studentFeeEntities = studentFeeRepository.findAll(customQuerySpecification);
            totalElement = studentFeeEntities.size();
        }

        List<StudentFeeDTO> studentFeeDTOS = studentFeeEntities.stream()
                .map(s -> {
                    StudentFeeDTO studentFeeDTO = modelMapper.map(s, StudentFeeDTO.class);

                    List<FeePaymentDTO> feePaymentDTOList = new ArrayList<>();
                    if (s.getFeePaymentEntities() != null && !s.getFeePaymentEntities().isEmpty()) {
                        for (FeePaymentEntity feePaymentEntity : s.getFeePaymentEntities()) {
                            FeePaymentDTO feePaymentDTO = modelMapper.map(feePaymentEntity, FeePaymentDTO.class);
                            feePaymentDTOList.add(feePaymentDTO);
                        }
                    }
                    studentFeeDTO.setFeePaymentDTOS(feePaymentDTOList);

                    return studentFeeDTO;
                }).collect(Collectors.toUnmodifiableList());

        log.info("Enter into getAllStudentsFeeByFilter");

        Map<String, Object> result = new HashMap<>();
        result.put("studentFeeDTOS", studentFeeDTOS);
        result.put("Total elements", totalElement);
        return result;
    }
}
