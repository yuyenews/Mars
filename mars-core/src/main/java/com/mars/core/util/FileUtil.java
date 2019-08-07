package com.mars.core.util;

import com.alibaba.fastjson.JSON;
import com.mars.core.logger.MarsLogger;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

/**
 * 文件帮助
 *
 * @author yuye
 */
public class FileUtil {

    private static MarsLogger logger = MarsLogger.getLogger(FileUtil.class);

    public static String local = null;

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
            if (local == null) {
                logger.error("", e);
            } else {
                logger.warn("自定义mybatis配置文件加载失败或者不存在，将自动使用默认配置");
            }
        } finally {
            try{
                reader.close();
                inputStream.close();
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
            try{
                inputStream.close();
            } catch (Exception e){
            }
        }
    }

    /**
     * 将file转化成二进制流
     *
     * @param file 文件流
     * @return 转化后的二进制流
     */
    public static byte[] getFileToByte(File file) {
        InputStream is = null;
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] by = new byte[(int) file.length()];
        try {
            is = new FileInputStream(file);
            byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            return bytestream.toByteArray();
        } catch (Exception ex) {
            logger.error("File转化成byte[]报错",ex);
            return null;
        } finally {
            try{
                bytestream.close();
                is.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 将InputStream转化成二进制流
     * @param inStream InputStream
     * @return 二进制流
     */
    public static byte[] getInputStreamToByte(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            return swapStream.toByteArray();
        } catch (Exception e) {
            logger.error("InputStream转化成byte[]报错",e);
            return null;
        } finally {
            try{
                swapStream.close();
            } catch (Exception e) {
            }
        }

    }

    public static byte[] getBufferedImageToByte(BufferedImage bufferedImage){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            ImageIO.write(bufferedImage, "gif", out);
            return out.toByteArray();
        } catch (Exception e){
            logger.error("BufferedImage转化成byte[]报错",e);
            return null;
        } finally {
            try{
                out.close();
            } catch (Exception e) {
            }
        }

    }
}
