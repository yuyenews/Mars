package com.mars.iserver.par.formdata;

import com.mars.server.server.request.model.MarsFileUpLoad;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析FormData
 */
public class ParsingFormData {

    public static final String PARAMS_KEY = "paramsKey";
    public static final String FILES_KEY = "filesKey";

    /**
     * 解析
     * @param inputStream 输入流
     * @param marsParams 参数
     * @param files 文件
     * @param contentType 内容类型
     * @return 参数和文件
     */
    public static Map<String,Object> parsing(InputStream inputStream, Map<String, List<String>> marsParams, Map<String, MarsFileUpLoad> files, String contentType) throws Exception {
        Map<String,Object> result = new HashMap<>();

        byte[] boundary = boundary(contentType).getBytes("UTF-8");
        byte[] endBoundary = endBoundary(contentType).getBytes("UTF-8");



        result.put(PARAMS_KEY,"");
        result.put(FILES_KEY,"");
        return result;
    }

    /**
     * 分隔符
     * @param contentType 内容类型
     * @return 分隔符
     */
    private static String boundary(String contentType){
        String bo = contentType.substring(contentType.indexOf(";"));
        return "--"+(bo.substring(bo.indexOf("=")+1).trim());
    }

    /**
     * 结束时的分隔符
     * @param contentType 内容类型
     * @return 结束时的分隔符
     */
    private static String endBoundary(String contentType){
        return boundary(contentType)+"--";
    }
}
