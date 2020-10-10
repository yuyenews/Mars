package com.mars.iserver.constant;

/**
 * 参数传递方式
 */
public class ParamTypeConstant {

    /**
     * 常规表单提交
     */
    public static final String URL_ENCODED = "application/x-www-form-urlencoded";

    /**
     * json提交
     */
    public static final String JSON = "application/json";

    /**
     * formData提交
     */
    public static final String FORM_DATA = "multipart/form-data";

    /**
     * 是否是json格式
     * @param contentType 内容类型
     * @return
     */
    public static boolean isJSON(String contentType){
        if(contentType == null){
            return false;
        }
        return contentType.startsWith(JSON) || contentType.equals(JSON);
    }

    /**
     * 是否是formData格式
     * @param contentType 内容类型
     * @return
     */
    public static boolean isFormData(String contentType){
        if(contentType == null){
            return false;
        }
        return contentType.startsWith(FORM_DATA) || contentType.equals(FORM_DATA);
    }

    /**
     * 是否是表单格式
     * @param contentType 内容类型
     * @return
     */
    public static boolean isUrlEncoded(String contentType){
        if(contentType == null){
            return false;
        }
        return contentType.startsWith(URL_ENCODED) || contentType.equals(URL_ENCODED);
    }
}
