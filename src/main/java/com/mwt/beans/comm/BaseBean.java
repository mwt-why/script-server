package com.mwt.beans.comm;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseBean implements Serializable {

    private LocalDateTime lastUpdateTime;

}
