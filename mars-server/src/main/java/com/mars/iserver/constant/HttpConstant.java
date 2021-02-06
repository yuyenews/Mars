package com.mars.iserver.constant;

import com.mars.common.constant.MarsConstant;

public class HttpConstant {

    /**
     * 回车换行符
     */
    public static String CARRIAGE_RETURN = "\r\n";

    /**
     * 头结束标识
     */
    public static String HEAD_END = "\r\n\r\n";

    /**
     * 冒号分割符
     */
    public static String SEPARATOR = ":";

    /**
     * 响应的基础信息
     */
    public static final String BASIC_RESPONSE = "HTTP/1.1 {statusCode} OK" + CARRIAGE_RETURN +
            "Vary: Accept-Encoding";

    /**
     * 响应的内容类型
     */
    public static final String RESPONSE_CONTENT_TYPE = "application/json;charset=" + MarsConstant.ENCODING;
}
