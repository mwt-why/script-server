package com.mwt.beans.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "taskRuntime")
public class TaskRuntime {
    @Id
    private String id;

    private String devId;

    private String taskId;

    private String curScript;

    private String curScriptRuntime;    //当前脚本运行时长

    private String curAccount;

    private String curRole;

    private String curMethod;

    private String curMethodRunTime;    //当前方法运行时间长

    private String curMethodOverTime;   //当前方法超时时间

    private String occurException;

    private int status; //0运行状态，1可能遇到问题
}
