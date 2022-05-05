package com.mwt.beans.task;

import com.mwt.beans.comm.BaseBean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(value = "enter")
public class Enter extends BaseBean {

    @Id
    private String id;

    private Integer deviceId;

    private String name;

    private List<String> scripts;

    private int status; //0正在进行，-1停止

    private int steps;

    private int loopCount;  //循环次数

    private LocalDateTime createTime;
}
