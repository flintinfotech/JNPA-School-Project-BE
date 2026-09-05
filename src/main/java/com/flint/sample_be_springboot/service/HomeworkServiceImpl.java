package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.HomeworkDTO;
import com.flint.sample_be_springboot.entity.HomeworkEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.HomeworkRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HomeworkServiceImpl extends BaseService implements HomeworkService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Override
    public HomeworkDTO getHomeworkById(Long homeworkId) {
        log.info("Enter into getHomeworkById");

        HomeworkEntity homeworkEntity = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new CustomException("Homework not found", HttpStatus.NOT_FOUND));

        HomeworkDTO homeworkDTO = modelMapper.map(homeworkEntity, HomeworkDTO.class);

        log.info("Exit from getHomeworkById");
        return homeworkDTO;
    }

    @Override
    public HomeworkDTO saveHomework(HomeworkDTO homeworkDTO) {
        log.info("Enter into saveHomework");

        if (homeworkDTO == null) {
            throw new CustomException("Homework DTO should not be null ", HttpStatus.CONFLICT);
        }

        boolean exists = homeworkRepository
                .existsBySubjectAndStandardAndDivisionAndMediumAndAcademicYearAndHomeworkDate(
                        homeworkDTO.getSubject(),
                        homeworkDTO.getStandard(),
                        homeworkDTO.getDivision(),
                        homeworkDTO.getMedium(),
                        homeworkDTO.getAcademicYear(),
                        homeworkDTO.getHomeworkDate()
                );

        if (exists) {
            throw new CustomException("Homework already exists for this subject on this date " + homeworkDTO.getHomeworkDate(), HttpStatus.CONFLICT);
        }

        HomeworkEntity homeworkEntity = modelMapper.map(homeworkDTO, HomeworkEntity.class);
        if(homeworkDTO.getUploadedFile() != null){
            homeworkEntity.setUploadedFile(Base64.getDecoder().decode(homeworkDTO.getUploadedFile()));
        }

        homeworkEntity.setAuditDetails(addAuditDetails(homeworkEntity.getAuditDetails()));

        HomeworkEntity savedEntity = homeworkRepository.save(homeworkEntity);
        HomeworkDTO savedDTO = modelMapper.map(savedEntity, HomeworkDTO.class);

        log.info("Exit from saveHomework");
        return savedDTO;
    }

    @Override
    public HomeworkDTO updateHomework(HomeworkDTO homeworkDTO) {

        log.info("Enter into updateHomework");

        if (homeworkDTO == null) {
            throw new CustomException(
                    "Homework DTO should not be null",
                    HttpStatus.PRECONDITION_FAILED
            );
        }

        if (homeworkDTO.getHomeworkId() == null) {
            throw new CustomException(
                    "Homework ID should not be null",
                    HttpStatus.PRECONDITION_FAILED
            );
        }

        HomeworkEntity existingHomework = homeworkRepository.findById(homeworkDTO.getHomeworkId())
                .orElseThrow(() -> new CustomException("Homework not found", HttpStatus.NOT_FOUND));

        // Check duplicate homework
        boolean exists = homeworkRepository
                .existsBySubjectAndStandardAndDivisionAndMediumAndAcademicYearAndHomeworkDateAndHomeworkIdNot(
                        homeworkDTO.getSubject(),
                        homeworkDTO.getStandard(),
                        homeworkDTO.getDivision(),
                        homeworkDTO.getMedium(),
                        homeworkDTO.getAcademicYear(),
                        homeworkDTO.getHomeworkDate(),
                        homeworkDTO.getHomeworkId()
                );

        if (exists) {
            throw new CustomException("Homework already exists for this subject on this date " + homeworkDTO.getHomeworkDate(),
                    HttpStatus.CONFLICT
            );
        }

        // Update fields
        existingHomework.setSubject(homeworkDTO.getSubject());
        existingHomework.setStandard(homeworkDTO.getStandard());
        existingHomework.setDivision(homeworkDTO.getDivision());
        existingHomework.setMedium(homeworkDTO.getMedium());
        existingHomework.setAcademicYear(homeworkDTO.getAcademicYear());
        existingHomework.setHomeworkDate(homeworkDTO.getHomeworkDate());
        existingHomework.setRemark(homeworkDTO.getRemark());

        // Update file only if a new file is provided
        if (homeworkDTO.getUploadedFile() != null &&
                !homeworkDTO.getUploadedFile().isEmpty()) {

            existingHomework.setUploadedFile(
                    Base64.getDecoder().decode(
                            homeworkDTO.getUploadedFile()
                    )
            );
        } else {
            existingHomework.setUploadedFile(null);
        }

        existingHomework.setAuditDetails(addAuditDetails(existingHomework.getAuditDetails()));

        HomeworkEntity updatedEntity = homeworkRepository.save(existingHomework);

        HomeworkDTO updatedDTO = modelMapper.map(updatedEntity, HomeworkDTO.class);

        log.info("Exit from updateHomework");

        return updatedDTO;
    }

    @Override
    public String deleteHomework(Long homeworkId) {
        log.info("Enter into deleteHomework");

        HomeworkEntity homeworkEntity = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new CustomException("Homework not found", HttpStatus.NOT_FOUND));

        homeworkRepository.delete(homeworkEntity);

        log.info("Exit from deleteHomework");
        return "Record deleted successfully";
    }

    @Override
    public Map<String, Object> getAllHomeworkByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllHomeworkByFilter");

        Page<HomeworkEntity> homeworkEntityPage;
        List<HomeworkEntity> homeworkEntities;
        long totalElement;

        CustomQuerySpecification<HomeworkEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if (paginate) {
            homeworkEntityPage = homeworkRepository.findAll(customQuerySpecification, pageable);
            homeworkEntities = homeworkEntityPage.getContent();
            totalElement = homeworkEntityPage.getTotalElements();
        } else {
            homeworkEntities = homeworkRepository.findAll(customQuerySpecification);
            totalElement = homeworkEntities.size();
        }

        List<HomeworkDTO> homeworkDTOS = homeworkEntities.stream()
                .map(h -> modelMapper.map(h, HomeworkDTO.class))
                .collect(Collectors.toUnmodifiableList());

        log.info("Exit from getAllHomeworkByFilter");
        Map<String, Object> map = new HashMap<>();
        map.put("Homework list", homeworkDTOS);
        map.put("total elements", totalElement);

        return map;
    }
}
