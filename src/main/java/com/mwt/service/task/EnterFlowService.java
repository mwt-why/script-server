package com.mwt.service.task;

import com.mwt.beans.task.EnterFlow;

public interface EnterFlowService {

    void save(EnterFlow enterFlow);

    EnterFlow findOneByDeviceId(int deviceId);

    void update(String id, EnterFlow enterFlow);

    void updateByDeviceId(int deviceId, EnterFlow enterFlow);
}
