package com.mars.core.base.config;

import com.mars.core.base.config.model.CrossDomainConfig;
import com.mars.core.base.config.model.FileUploadConfig;
import com.mars.core.base.config.model.JedisConfig;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 配置信息
 */
public abstract class MarsConfig {

    /**
     * 端口号
     * @return 端口号
     */
    public int port(){
        return 8080;
    }

    /**
     * jwt失效时间
     * @return jwt失效时间
     */
    public int jwtTime(){
        return 86400;
    }

    /**
     * 上传文件大小配置
     * @return 上传文件大小配置
     */
    public FileUploadConfig fileUploadConfig(){
        return new FileUploadConfig();
    }

    /**
     * 跨域配置
     * @return 跨域配置
     */
    public CrossDomainConfig crossDomainConfig(){
        return new CrossDomainConfig();
    }

    /**
     * 连接池配置
     * @return 连接池配置
     */
    public List<Properties> jdbcProperties(){
        return null;
    }

    /**
     * jedis配置
     * @return jedis配置
     */
    public JedisConfig jedisConfig(){
        return new JedisConfig();
    }

    /**
     * 其他配置项
     * @return
     */
    public Map<String,String> marsValues(){
        return null;
    }
}
