package com.mars.netty.util;

import com.mars.server.server.request.model.MarsFileUpLoad;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUpLoad {

    /**
     * 默认单个文件2M
     */
    private static int fileSizeMax = 2*1024*1024;

    /**
     * 默认总文件数10M
     */
    private static int sizeMax = 10*1024*1024;

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
                    }
                    MarsFileUpLoad marsFileUpLoad = new MarsFileUpLoad();
                    marsFileUpLoad.setName(item.getFieldName());
                    marsFileUpLoad.setInputStream(item.getInputStream());
                    marsFileUpLoad.setFileName(item.getName());
                    files.put(marsFileUpLoad.getName(),marsFileUpLoad);
                }
            }
            return files;
        } catch (Exception e){
            throw new Exception("接受上传的文件出错",e);
        }
    }

    public static List<FileItem> getFileItem(HttpServletRequest request) throws Exception {

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        fileUpload.setFileSizeMax(fileSizeMax);
        fileUpload.setSizeMax(sizeMax);
        List<FileItem> fileItemList = fileUpload.parseRequest(new ServletRequestContext(request));

        return fileItemList;
    }
}
