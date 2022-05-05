package com.mwt.service.task;

import com.mwt.beans.task.Enter;

public interface EnterService {

    void add(Enter enter);

    Enter findOneByDeviceId(int deviceId);

    void update(String id, Enter enter);

    void updateByDeviceId(int deviceId, Enter enter);

    void deleteExpiring();
}
