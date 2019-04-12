package com.mars.core.logger;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MarsLog4jUtil {

    /**
     * 加载log4j配置文件
     * @throws Exception
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
     * @throws Exception
     */
    private static void initLog4jPath(InputStream inputStream) throws Exception {
        try {
            BufferedInputStream in = new BufferedInputStream(inputStream);
            ConfigurationSource source = new ConfigurationSource(in);
            Configurator.initialize(null, source);

            inputStream.close();
            in.close();
        } catch (Exception e){
            throw new Exception("logFile指向的路径下找不到相应的文件",e);
        }
    }
}
