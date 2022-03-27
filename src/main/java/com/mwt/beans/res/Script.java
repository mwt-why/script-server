package com.mwt.beans.res;

import com.mwt.beans.comm.BaseBean;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(value = "script")
public class Script extends BaseBean {
    @Id
    private String id;

    /**
     * 游戏
     */
    private String game;

    /**
     * 脚本的名字
     */
    private List<String> scripts;

    /**
     * 构建执行的临时脚本
     */
    private List<String> tmpScripts;

    /**
     * 当前脚本
     */
    private String curScript;

    /**
     * 下一个执行脚本
     */
    private String nextScript;

}
