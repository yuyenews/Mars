package com.martian.config;

import com.martian.config.model.CrossDomainConfig;
import com.martian.config.model.FileUploadConfig;
import com.martian.config.model.RequestConfig;
import io.magician.common.event.EventGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

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
     * worker事件管理器配置
     * @return worker事件管理器
     */
    public EventGroup workerEventGroup(){
        return new EventGroup(5, Executors.newCachedThreadPool());
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