package com.mwt.beans.task;

import com.mwt.beans.comm.BaseBean;
import com.mwt.beans.res.Account;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 持久的任务
 */
@Data
@Builder
@Document(value = "enduringTask")
public class EnduringTask extends BaseBean {

    @Id
    private String id;

    /**
     * -1停止，0创建中，1执行中
     */
    private int status;

    /**
     * 是否是队长任务
     */
    private Boolean isLeader;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 好友任务的id，每次做任务的时候一起组队的任务
     */
    private List<String> friends;

    /**
     * 账号信息
     */
    private List<TaskAccount> accountBeans;

    /**
     * 当前执行账号
     */
    private String curAccount;

    /**
     * 当前执行脚本
     */
    private String curScript;

    /**
     * 该任务执行了多少次
     */
    private int count;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
