package com.mars.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MarsLog4jUtil {

    /**
     * 加载log4j配置文件
     *
     * @param config 配置文件
     * @throws Exception 异常
     */
    public static void initLog4jConfig(JSONObject config) throws Exception {
        String path = config.getString("logFile");
        if(path != null){
            if(path.startsWith("classPath-")){
                String logCfgPath = path.replace("classPath-","");
                InputStream inputStream = MarsLog4jUtil.class.getResourceAsStream("/"+logCfgPath);
                initLog4jPath(inputStream);
            } else {
                File file = new File(path);
                initLog4jPath(new FileInputStream(file));
            }
        }
    }

    /**
     * 加载log4j配置文件
     * @param inputStream 文件流
     * @throws Exception 异常
     */
    private static void initLog4jPath(InputStream inputStream) throws Exception {
        try {
            ConfigurationSource source = new ConfigurationSource(inputStream);
            Configurator.initialize(null, source);
            inputStream.close();
        } catch (Exception e){
            throw new Exception("无法加载logFile指向的路径下的文件",e);
        }
    }
}
