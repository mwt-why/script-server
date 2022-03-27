package com.mwt.beans.comm;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Seq {
    @Id
    private String id;

    private int seq;
}
