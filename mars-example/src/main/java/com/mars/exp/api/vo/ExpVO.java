package com.mars.exp.api.vo;

import com.mars.common.annotation.api.MarsDataCheck;
import com.mars.server.server.request.model.MarsFileUpLoad;

/**
 * 所有实体类都支持lombok，这里为了兼容所有的环境，就还是采用的原始的get，set
 */
public class ExpVO {

    @MarsDataCheck(notNull = true, msg = "名称不可以为空")
    private String name;

    @MarsDataCheck(notNull = true, msg = "名称们不可以为空")
    private String[] names;

    private MarsFileUpLoad marsFileUpLoad;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public MarsFileUpLoad getMarsFileUpLoad() {
        return marsFileUpLoad;
    }

    public void setMarsFileUpLoad(MarsFileUpLoad marsFileUpLoad) {
        this.marsFileUpLoad = marsFileUpLoad;
    }
}
