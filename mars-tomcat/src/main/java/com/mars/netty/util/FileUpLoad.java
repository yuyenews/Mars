package com.mars.netty.util;

import com.mars.core.base.config.model.FileUploadConfig;
import com.mars.core.util.MarsConfiguration;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FileUpLoad {

    /**
     * 获取文件列表
     * @param request 请求
     * @return 返回
     * @throws Exception 异常
     */
    public static List<FileItem> getFileItem(HttpServletRequest request) throws Exception {
        if(!ServletFileUpload.isMultipartContent(request)) {
            return null;
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        FileUploadConfig fileUploadConfig = MarsConfiguration.getConfig().fileUploadConfig();

        fileUpload.setFileSizeMax(fileUploadConfig.getFileSizeMax());
        fileUpload.setSizeMax(fileUploadConfig.getSizeMax());

        List<FileItem> fileItemList = fileUpload.parseRequest(new ServletRequestContext(request));

        return fileItemList;
    }
}
