package com.mars.iserver.par.formdata;

import com.mars.common.base.config.model.FileUploadConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.mars.server.server.request.model.MarsFileUpLoad;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析FormData
 */
public class ParsingFormData {

    /**
     * 参数key
     */
    public static final String PARAMS_KEY = "paramsKey";
    /**
     * 文件key
     */
    public static final String FILES_KEY = "filesKey";

    /**
     * 解析
     * @param exchange 请求对象
     * @param marsParams 参数
     * @param files 文件
     * @param contentType 内容类型
     * @return 参数和文件
     */
    public static Map<String,Object> parsing(HttpExchange exchange, Map<String, List<String>> marsParams, Map<String, MarsFileUpLoad> files, String contentType) throws Exception {
        Map<String,Object> result = new HashMap<>();

        List<FileItem> fileItemList = getFileItem(exchange,contentType);

        for(FileItem item : fileItemList){
            if(item.isFormField()){
                String name = item.getFieldName();
                String value = item.getString(MarsConstant.ENCODING);
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

        result.put(PARAMS_KEY,marsParams);
        result.put(FILES_KEY,files);
        return result;
    }

    /**
     * 获取文件列表
     * @param request 请求
     * @param contentType 请求类型
     * @return 返回
     * @throws Exception 异常
     */
    public static List<FileItem> getFileItem(HttpExchange request, String contentType) throws Exception {

        FileItemFactory factory = new DiskFileItemFactory();

        FileUploadConfig fileUploadConfig = MarsConfiguration.getConfig().fileUploadConfig();

        FileUploadBase fileUploadBase = new HttpExchangeFileUpload();
        fileUploadBase.setFileItemFactory(factory);

        fileUploadBase.setFileSizeMax(fileUploadConfig.getFileSizeMax());
        fileUploadBase.setSizeMax(fileUploadConfig.getSizeMax());

        List<FileItem> fileItemList = fileUploadBase.parseRequest(new HttpExchangeRequestContext(request,contentType));

        return fileItemList;
    }
}
