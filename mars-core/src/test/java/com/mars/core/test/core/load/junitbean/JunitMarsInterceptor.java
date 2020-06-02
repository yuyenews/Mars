package com.mars.core.test.core.load.junitbean;

import com.mars.common.annotation.api.MarsInterceptor;
import com.mars.common.annotation.bean.MarsWrite;

@MarsInterceptor
public class JunitMarsInterceptor {

    @MarsWrite
    private JunitMarsBean junitMarsBean;

    public JunitMarsBean getJunitMarsBean() {
        return junitMarsBean;
    }

    public void setJunitMarsBean(JunitMarsBean junitMarsBean) {
        this.junitMarsBean = junitMarsBean;
    }
}
