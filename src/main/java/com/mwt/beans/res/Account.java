package com.mwt.beans.res;

import com.mwt.beans.comm.BaseBean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(value = "account")
public class Account extends BaseBean {

    @Id
    private String id;

    private String account;

    private String password;

    private int type;   //0邮箱，1手机

    private int status; //0正常，1正在使用，-1不可使用
}
