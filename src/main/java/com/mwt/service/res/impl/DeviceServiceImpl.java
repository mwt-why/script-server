package com.mwt.service.res.impl;

import com.mwt.beans.comm.DevSeq;
import com.mwt.beans.res.Device;
import com.mwt.repository.AutoIdGenerator;
import com.mwt.repository.res.DeviceRepository;
import com.mwt.service.res.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final String COLLECTION_NAME = "device";

    @Resource
    private AutoIdGenerator autoIdGenerator;

    @Resource
    private DeviceRepository deviceRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Device> list(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    @Override
    public int save(Device device) {
        int nextSeq = autoIdGenerator.getNextSeq(DevSeq.class);
        device.setId(nextSeq);
        device.setLastUpdateTime(LocalDateTime.now());
        deviceRepository.save(device);
        return nextSeq;
    }

    @Override
    public int check(String serial) {
        Device device = mongoTemplate.findOne(Query.query(Criteria.where("serial").is(serial)), Device.class);
        if (Objects.nonNull(device)) {
            return device.getId();
        }
        return 0;
    }
}
