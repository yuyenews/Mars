package com.mars.common.test.config;

import com.mars.common.base.config.MarsConfig;

public class TestMarsConfig extends MarsConfig {

    @Override
    public int port() {
        return 8080;
    }
}
