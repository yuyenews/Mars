package com.mars.tomcat.util;

import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.model.MarsFileUpLoad;
import org.apache.tomcat.util.http.fileupload.FileItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 如果是文件上传的提交，处理表单字段和文件流
 */
public class FileItemUtil {

    /**
     * 如果是文件上传的提交，处理表单字段和文件流
     * @param fileItemList 文件列表
     * @param marsRequest mars请求
     * @return 加工后的mars请求
     * @throws Exception 异常
     */
    public static HttpMarsRequest getHttpMarsRequest(List<FileItem> fileItemList, HttpMarsRequest marsRequest) throws Exception{
        if(fileItemList != null){
            Map<String, MarsFileUpLoad> files = new HashMap<>();
            Map<String,List<String>> marsParams = new HashMap<>();
            for(FileItem item : fileItemList){
                if(item.isFormField()){
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    List<String> params = marsParams.get(name);
                    if(params == null){
                        params = new ArrayList<>();
                    }
                    params.add(value);
                    marsParams.put(name,params);
                } else {
                    MarsFileUpLoad marsFileUpLoad = new MarsFileUpLoad();
                    marsFileUpLoad.setName(item.getFieldName());
                    marsFileUpLoad.setInputStream(item.getInputStream());
                    marsFileUpLoad.setFileName(item.getName());
                    files.put(marsFileUpLoad.getName(),marsFileUpLoad);
                }
            }
            marsRequest.setFiles(files);
            marsRequest.setParams(marsParams);
        }
        return marsRequest;
    }
}
