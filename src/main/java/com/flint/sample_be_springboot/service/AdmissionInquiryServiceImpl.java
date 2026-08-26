package com.flint.sample_be_springboot.service;


import com.flint.sample_be_springboot.dto.AdmissionInquiryDTO;

import com.flint.sample_be_springboot.entity.AdmissionInquiry;

import com.flint.sample_be_springboot.exception.CustomException;
import com.flint.sample_be_springboot.repository.AdmissionInquiryRepository;
import com.flint.sample_be_springboot.util.BaseService;
import com.flint.sample_be_springboot.util.CustomQuerySpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdmissionInquiryServiceImpl extends BaseService implements AdmissionInquiryService {


    @Autowired
    private AdmissionInquiryRepository admissionInquiryRepository;


    ModelMapper modelMapper = new ModelMapper();


    public AdmissionInquiryDTO saveAdmissionInquiry(AdmissionInquiryDTO admissionInquiryDTO)
    {
        if (admissionInquiryDTO == null)
        {
            throw new CustomException("Form info cannot be null", HttpStatus.PRECONDITION_FAILED);
        }

        AdmissionInquiry admissionInquiry = modelMapper.map(admissionInquiryDTO, AdmissionInquiry.class);

        admissionInquiry.setStatus("NEW");
        admissionInquiry.setAuditDetails(addAuditDetails(admissionInquiry.getAuditDetails()));


        AdmissionInquiry savedInquiry = admissionInquiryRepository.save(admissionInquiry);
        AdmissionInquiryDTO savedDTO = modelMapper.map(savedInquiry, AdmissionInquiryDTO.class);

        return savedDTO;
    }


    public AdmissionInquiryDTO updateAdmissionInquiryById(Long id, AdmissionInquiryDTO admissionInquiryDTO)
    {
        Optional<AdmissionInquiry> existingAdmissionInquiry = admissionInquiryRepository.findById(id);

        if (existingAdmissionInquiry.isPresent())
        {
            AdmissionInquiry inquiry = existingAdmissionInquiry.get();

            inquiry.setFirstName(admissionInquiryDTO.getFirstName());
            inquiry.setLastName(admissionInquiryDTO.getLastName());
            inquiry.setContactNumber(admissionInquiryDTO.getContactNumber());
            inquiry.setStandard(admissionInquiryDTO.getStandard());
            inquiry.setMedium(admissionInquiryDTO.getMedium());
            inquiry.setStream(admissionInquiryDTO.getStream());
            inquiry.setStatus(admissionInquiryDTO.getStatus());

            inquiry.setAuditDetails(addAuditDetails(inquiry.getAuditDetails()));

            AdmissionInquiry updatedInquiry = admissionInquiryRepository.save(inquiry);

            admissionInquiryDTO = modelMapper.map(updatedInquiry, AdmissionInquiryDTO.class);

        }
        else
        {
            throw new CustomException("Inquiry form is not exist with id: " + id, HttpStatus.NOT_FOUND);
        }

        return admissionInquiryDTO;
    }

    public AdmissionInquiryDTO deleteAdmissionInquiryById(Long id)
    {
        Optional<AdmissionInquiry> existingInquiry = admissionInquiryRepository.findById(id);

        if (existingInquiry.isPresent()) {

            AdmissionInquiry inquiry = existingInquiry.get();

            AdmissionInquiryDTO inquiryDTO = modelMapper.map(inquiry, AdmissionInquiryDTO.class);

            admissionInquiryRepository.delete(inquiry);

            return inquiryDTO;

        } else {
            throw new CustomException("Form is not exist with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    public Map<String, Object> getAllAdmissionInquiryByFilter(
            Map<String, Object> filter,
            Pageable pageable,
            boolean paginate) {

        List<AdmissionInquiry> inquiries;
        List<AdmissionInquiryDTO> admissionInquiryDTOS = new ArrayList<>();
        long totalElement;

        CustomQuerySpecification<AdmissionInquiry> specification =CustomQuerySpecification.getInstance(filter);

        Sort sort = Sort.by(Sort.Direction.DESC, "admissionInquiryId");

        if (paginate) {

            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
            Page<AdmissionInquiry> pageResult =admissionInquiryRepository.findAll(specification, sortedPageable);
            inquiries = pageResult.getContent();
            totalElement = pageResult.getTotalElements();
        } else {
            inquiries = admissionInquiryRepository.findAll(specification, sort);
            totalElement = inquiries.size();
        }

        for (AdmissionInquiry inquiry : inquiries) {
            AdmissionInquiryDTO dto =modelMapper.map(inquiry, AdmissionInquiryDTO.class);
            admissionInquiryDTOS.add(dto);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("Data", admissionInquiryDTOS);
        result.put("Total", totalElement);

        return result;
    }


}
