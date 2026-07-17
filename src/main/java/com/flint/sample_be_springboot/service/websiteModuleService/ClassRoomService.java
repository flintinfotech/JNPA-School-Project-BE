package com.flint.sample_be_springboot.service.websiteModuleService;

import com.flint.sample_be_springboot.dto.websiteModuleDTOS.classRoom.ClassRoomDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ClassRoomService {

    ClassRoomDTO getClassRoomById(Long classRoomId);

    ClassRoomDTO saveClassRoom(ClassRoomDTO classRoomDTO);

    ClassRoomDTO updateClassRoom(ClassRoomDTO classRoomDTO);

    String deleteClassRoom(Long classRoomId);

    Map<String, Object> getAllClassRoomsByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);

}
