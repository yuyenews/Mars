package com.mars.netty.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
import com.mars.server.server.request.model.MarsFileUpLoad;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUpLoad {

    private static Logger logger = LoggerFactory.getLogger(FileUpLoad.class);

    /**
     * 默认单个文件2M
     */
    private static int fileSizeMax = 2*1024*1024;

    /**
     * 默认总文件数10M
     */
    private static int sizeMax = 10*1024*1024;

    /**
     * 获取文件Map
     * @param request 请求
     * @return 返回
     * @throws Exception 异常
     */
    public static Map<String, MarsFileUpLoad> getFiles(HttpServletRequest request) throws Exception {
        try {
            if(!ServletFileUpload.isMultipartContent(request)) {
                return null;
            }
            Map<String, MarsFileUpLoad> files = new HashMap<>();

            List<FileItem> fileItemList = getFileItem(request);
            if(fileItemList != null){
                for(FileItem item : fileItemList){
                    if(item.isFormField()){
                        continue;
                    } else {
                        MarsFileUpLoad marsFileUpLoad = new MarsFileUpLoad();
                        marsFileUpLoad.setName(item.getFieldName());
                        marsFileUpLoad.setInputStream(item.getInputStream());
                        marsFileUpLoad.setFileName(item.getName());
                        files.put(marsFileUpLoad.getName(),marsFileUpLoad);
                    }
                }
            }
            return files;
        } catch (Exception e){
            throw new Exception("接受上传的文件出错",e);
        }
    }

    /**
     * 获取文件列表
     * @param request 请求
     * @return 返回
     * @throws Exception 异常
     */
    public static List<FileItem> getFileItem(HttpServletRequest request) throws Exception {
        init();

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        fileUpload.setFileSizeMax(fileSizeMax);
        fileUpload.setSizeMax(sizeMax);
        List<FileItem> fileItemList = fileUpload.parseRequest(new ServletRequestContext(request));

        return fileItemList;
    }

    /**
     * 初始化
     */
    private static void init(){
        try {
            JSONObject config = ConfigUtil.getConfig();
            JSONObject fileUpload = config.getJSONObject("fileUpload");
            if(fileUpload != null){
                Integer configFileSizeMax = fileUpload.getInteger("fileSizeMax");
                Integer configSizeMax = fileUpload.getInteger("sizeMax");

                if(configFileSizeMax != null && configFileSizeMax > 0){
                    fileSizeMax = configFileSizeMax;
                }
                if(configSizeMax != null && configSizeMax > 0){
                    sizeMax = configSizeMax;
                }
            }
        } catch (Exception e){
            logger.error("设置文件大小限制参数异常",e);
        }
    }
}
