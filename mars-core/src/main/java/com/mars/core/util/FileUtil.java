package com.mars.core.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;

/**
 * 文件帮助
 *
 * @author yuye
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 根据文件路径 获取文件中的字符串内容
     *
     * @param path 路径
     * @return str
     */
    public static String readFileString(String path) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = FileUtil.class.getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try{
                if(reader != null){
                    reader.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e){
            }
        }
        return null;
    }

    /**
     * 根据文件路径 获取yml配置文件
     *
     * @param path 路径
     * @return str
     * @throws Exception 异常
     */
    public static String readYml(String path) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = FileUtil.class.getResourceAsStream(path);
            HashMap testEntity = new Yaml().loadAs(inputStream,HashMap.class);
            return JSON.toJSONString(testEntity);
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }
}
