package com.mwt.service.res;

import com.mwt.beans.res.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {
    Page<Device> list(Pageable pageable);

    int save(Device device);

    int check(String serial);
}
