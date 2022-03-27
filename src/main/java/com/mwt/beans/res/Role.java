package com.mwt.beans.res;

import com.mwt.beans.comm.BaseBean;
import com.mwt.beans.comm.Coordinate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "role")
public class Role extends BaseBean {

    @Id
    private String id;

    private String roleId;

    private String name;

    private String level;

    private Coordinate coordinate;

    private int status;

}
