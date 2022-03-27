package com.mwt.service.res.impl;

import com.mwt.beans.res.Device;
import com.mwt.repository.res.DeviceRepository;
import com.mwt.service.res.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceRepository deviceRepository;

    @Override
    public Page<Device> list(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }
}
