package com.mwt.beans.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(value = "leader")
public class Leader {
    @Id
    private String id;

    private String leaderId;

    private int status; //0空闲，1正在做任务

    private LocalDateTime lastUpdateTime;
}
