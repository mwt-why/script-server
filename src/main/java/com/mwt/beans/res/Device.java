package com.mwt.beans.res;

import com.mwt.beans.comm.BaseBean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "device")
public class Device extends BaseBean {

    @Id
    private int id;

    private String serial;

    private int status; //0可用，1使用中，-1不可用
}
