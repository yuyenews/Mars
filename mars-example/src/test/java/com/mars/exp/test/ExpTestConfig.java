package com.mars.exp.test;

import com.mars.common.base.config.MarsConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 更多配置项，请到官网查看官方文档
 */
public class ExpTestConfig extends MarsConfig {

    /**
     * 配置数据源
     * @return
     */
    @Override
    public List<Properties> jdbcProperties() {
        List<Properties> list = new ArrayList<>();

        // 用的是阿里巴巴的 druid数据源，其他属性可自行查阅
        Properties properties = new Properties();
        properties.put("name","dataSource");
        properties.put("url","");
        properties.put("username","");
        properties.put("password","");
        properties.put("driverClassName","com.mysql.jdbc.Driver");

        list.add(properties);

        // 如果要多个数据源，add多个到list即可

        return list;
    }
}
