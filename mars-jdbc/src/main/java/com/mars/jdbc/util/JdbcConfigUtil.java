package com.mars.jdbc.util;

import com.mars.core.base.config.MarsConfig;
import com.mars.core.util.MarsConfiguration;

import java.util.List;
import java.util.Properties;

/**
 * JDBC配置
 */
public class JdbcConfigUtil {

    /**
     * 获取JDBC配置信息
     *
     * @return 配置信息
     */
    public static List<Properties> getJdbcConfig() throws Exception {
        try {
            MarsConfig jsonObject = MarsConfiguration.getConfig();
            List<Properties> propertiesList = jsonObject.jdbcProperties();
            return propertiesList;
        } catch (Exception e) {
            throw new Exception("从配置文件中读取jdbc模块配置出错",e);
        }
    }
}
