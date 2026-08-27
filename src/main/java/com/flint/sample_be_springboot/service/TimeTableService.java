package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.TimeTableDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface TimeTableService {

    TimeTableDTO saveTimeTable(TimeTableDTO timeTableDTO);

    TimeTableDTO getTimeTableByTimeTableId(Long timeTableId);

    TimeTableDTO updateTimeTable(TimeTableDTO timeTableDTO);

    String deleteTimeTable(Long tableId);

    Map<String, Object> getAllTimeTableByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate);
}
