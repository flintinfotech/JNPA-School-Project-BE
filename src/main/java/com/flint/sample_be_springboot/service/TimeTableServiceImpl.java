package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.dto.TimeTableDTO;
import com.flint.sample_be_springboot.util.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TimeTableServiceImpl extends BaseService implements TimeTableService {


    @Override
    public TimeTableDTO saveTimeTable(TimeTableDTO timeTableDTO) {
        return null;
    }

    @Override
    public TimeTableDTO getTimeTableByTableId(Long tableId) {
        return null;
    }

    @Override
    public TimeTableDTO updateTimeTable(TimeTableDTO timeTableDTO) {
        return null;
    }

    @Override
    public String deleteTimeTable(Long tableId) {
        return "";
    }

    @Override
    public Map<String, Object> getALlTimeTableByFilter(Map<String, Object> filter, Pageable pageable, boolean paginate) {
        return Map.of();
    }
}
