package com.mwt.beans.task;

import com.mwt.beans.comm.BaseBean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("enterFlow")
public class EnterFlow extends BaseBean {

    @Id
    private String id;

    private int type;   //0 init,1 main

    private int deviceId;

    private List<String> initEnters;

    private List<String> mainEnters;

    private List<String> enters;

    private String curEnter;

    private LocalDateTime createTime;
}
