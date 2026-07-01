package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.AcademicYearDTO;
import com.flint.sample_be_springboot.entity.AcademicYearEntity;
import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.AcademicYearRepository;
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

@Slf4j
@Service
public class AcademicServiceImpl implements AcademicYearService{

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    AcademicYearRepository academicYearRepository;

    @Override
    public AcademicYearDTO saveAcademicYear(AcademicYearDTO academicYearDTO) {
        log.info("Enter into saveAcademicYear");

        AcademicYearEntity academicYearEntity = academicYearRepository.findByAcademicYearName(academicYearDTO.getAcademicYearName());
        if(academicYearEntity != null){
            throw new CustomException("This year is already present", HttpStatus.PRECONDITION_FAILED);
        }

        // other validations

        AcademicYearEntity academicYear = modelMapper.map(academicYearDTO, AcademicYearEntity.class);
        AcademicYearEntity savedEntity = academicYearRepository.save(academicYear);
        AcademicYearDTO savedDTO = modelMapper.map(savedEntity, AcademicYearDTO.class);

        log.info("Exit from saveAcademicYear");
        return savedDTO;
    }

    @Override
    public AcademicYearDTO updateAcademicYear(AcademicYearDTO academicYearDTO) {
        log.info("Enter into updateAcademicYear");

        AcademicYearEntity existingAcademicYear = academicYearRepository.findById(academicYearDTO.getAcademicYearId())
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));

        academicYearRepository
                .findByAcademicYearNameAndAcademicYearIdNot(
                        academicYearDTO.getAcademicYearName(),
                        existingAcademicYear.getAcademicYearId())
                .ifPresent(entity -> {
                    throw new CustomException(
                            "This academic year already exists",
                            HttpStatus.PRECONDITION_FAILED
                    );
                });

        modelMapper.map(academicYearDTO, AcademicYearEntity.class);

        AcademicYearEntity updatedEntity = academicYearRepository.save(existingAcademicYear);
        AcademicYearDTO updatedDTO = modelMapper.map(updatedEntity, AcademicYearDTO.class);

        log.info("Exit from updateAcademicYear");
        return updatedDTO;
    }

    @Override
    public AcademicYearDTO getAcademicYearById(Long academicYearId) {
        log.info("Enter into getAcademicYearById");

        AcademicYearEntity academicYearEntity = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));

        AcademicYearDTO academicYearDTO = modelMapper.map(academicYearEntity, AcademicYearDTO.class);

        log.info("Exit from getAcademicYearById");
        return academicYearDTO;
    }

    @Override
    public String deleteAcademicYear(Long academicYearId) {
        log.info("Enter into deleteAcademicYear");

        AcademicYearEntity academicYearEntity = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new CustomException("Academic year not found", HttpStatus.PRECONDITION_FAILED));

        // validations

        academicYearRepository.delete(academicYearEntity);

        log.info("Enter into deleteAcademicYear");
        return "Academic year deleted successfully";
    }

    @Override
    public Map<String, Object> getAllAcademicYearsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        log.info("Enter into getAllAcademicYearsByFilter");

        List<AcademicYearEntity> academicYearEntities;
        Page<AcademicYearEntity> academicYearEntityPage;
        long totalElements;

        CustomQuerySpecification<AcademicYearEntity> customQuerySpecification = CustomQuerySpecification.getInstance(filter);

        if(paginate){
            academicYearEntityPage = academicYearRepository.findAll(customQuerySpecification, pageable);
            academicYearEntities = academicYearEntityPage.getContent();
            totalElements = academicYearEntityPage.getTotalElements();
        }else{
            academicYearEntities = academicYearRepository.findAll(customQuerySpecification);
            totalElements = academicYearEntities.size();
        }

        List<AcademicYearDTO> academicYearDTOS = academicYearEntities.stream()
                        .map(a -> modelMapper.map(a, AcademicYearDTO.class))
                                .toList();

        log.info("Exit from getAllAcademicYearsByFilter");
        Map<String, Object> result = new HashMap<>();
        result.put("Date", academicYearDTOS);
        result.put("Total", totalElements);
        return result;
    }

    @Override
    public AcademicYearDTO getCurrentAcademicYear(){
        return null;
    }

    @Override
    public AcademicYearDTO setCurrentAcademicYear(Long academicYearId){
        return null;
    }
}
