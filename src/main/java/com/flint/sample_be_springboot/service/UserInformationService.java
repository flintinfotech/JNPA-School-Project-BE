package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.UserInformationDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserInformationService {

    UserInformationDTO getUserInformationById(Long userInformationId);

    UserInformationDTO saveUserInformation(UserInformationDTO userInformationDTO);

    UserInformationDTO updateUserInformation(UserInformationDTO userInformationDTO);

    String deleteUserInformation(Long userInformationId);

    Map<String, Object> getAllUserInformationByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
