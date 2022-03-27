package com.mwt.service.task;

public interface LeaderService {

    void upset(String leaderId);

    void switchStatus(String leaderId, int status);

    boolean leaderIsDoing(String leaderId);
}
