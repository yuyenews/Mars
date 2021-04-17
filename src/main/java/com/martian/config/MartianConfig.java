package com.martian.config;

import com.martian.config.model.CrossDomainConfig;
import com.martian.config.model.FileUploadConfig;
import com.martian.config.model.RequestConfig;
import com.martian.config.model.ThreadPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置信息
 */
public abstract class MartianConfig {

    /**
     * 端口号
     * @return 端口号
     */
    public int port(){
        return 8080;
    }

    /**
     * 线程池配置
     * @return 线程池配置
     */
    public ThreadPoolConfig threadPoolConfig(){
        return new ThreadPoolConfig();
    }

    /**
     * 请求设置
     * @return
     */
    public RequestConfig requestConfig(){
        return new RequestConfig();
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
        List<Properties> propertiesList = new ArrayList<>();
        Properties properties = new Properties();
        properties.setProperty("name","default");
        propertiesList.add(properties);
        return propertiesList;
    }
}