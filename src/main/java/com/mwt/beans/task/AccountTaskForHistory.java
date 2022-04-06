package com.mwt.beans.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "accountTaskForHistory")
public class AccountTaskForHistory {

    @Id
    private String id;

    private String account;

    private String password;

    private int status; //0表示未执行，1表示执行中，-1表示已经执行过了
}
