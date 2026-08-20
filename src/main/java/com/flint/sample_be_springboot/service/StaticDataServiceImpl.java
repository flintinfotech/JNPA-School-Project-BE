package com.flint.sample_be_springboot.service;

import com.flint.sample_be_springboot.entity.StaticDataEntity;
import com.flint.sample_be_springboot.repository.StaticDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StaticDataServiceImpl implements StaticDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticDataServiceImpl.class);

    private final StaticDataRepository staticDataRepository;

    StaticDataServiceImpl(StaticDataRepository staticDataRepository) {
        this.staticDataRepository = staticDataRepository;
    }

    @Override
    public Map<String, List<String>> getAllStaticData() {
        LOGGER.info("Enter into getAllStaticData()");
        Map<String, List<String>> data = new HashMap<>();

        List<StaticDataEntity> list = staticDataRepository.findAll();
        if (!list.isEmpty()) {
            data = list.stream()
                    .collect(Collectors.groupingBy(StaticDataEntity::getDropDrownKey,
                            Collectors.mapping(StaticDataEntity::getDropDrownValue, Collectors.toList())));

        }
        LOGGER.info("Exit from getAllStaticData");
        return data;
    }
}
