package com.mwt.service.task;


import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Device;
import com.mwt.beans.task.EnduringTask;

import java.util.List;

public interface EnduringTaskService {

    int registerDevice(Device device);

    int checkDevice(String serial);

    EnduringTask getTask(String deviceId);

    EnduringTask getEnduringTaskById(String id);

    int deleteTask(String id);

    long resetAccountRole(String id);

    long tagCurAccountIsDo(String id);

    long begin(String id);

    long finish(String id);

    long updateTask(String id, SimpleMap simpleMap);

    long updateCurAccount(String id, SimpleMap simpleMap);

    long updateCurRole(String id, SimpleMap simpleMap);

    List<String> addFriends(String id);

    boolean checkLeaderAddFriendOver(String leaderId);

    long reportAddFriend(String id);

    long teamUp(String id);

}
