package com.mars.common.test.config;

import com.mars.common.base.config.MarsConfig;

import java.util.List;
import java.util.Properties;

public class TestMarsConfig extends MarsConfig {

    @Override
    public int port() {
        return 8080;
    }

    @Override
    public List<Properties> jdbcProperties() {
        return null;
    }
}
