package com.mars.core.test.core.load.junitbean;

import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsWrite;

@MarsBean("junitMarsBean")
public class JunitMarsBean {

    @MarsWrite
    private JunitIocMarsBean junitIocMarsBean;

    public JunitIocMarsBean getJunitIocMarsBean() {
        return junitIocMarsBean;
    }

    public void setJunitIocMarsBean(JunitIocMarsBean junitIocMarsBean) {
        this.junitIocMarsBean = junitIocMarsBean;
    }
}
