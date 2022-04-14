package com.mwt.beans.comm;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "temp")
public class Temp {

    @Id
    private String id;

    private String key;

    private Object value;
}
