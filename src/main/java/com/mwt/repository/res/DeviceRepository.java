package com.mwt.repository.res;

import com.mwt.beans.res.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, Integer> {
}
