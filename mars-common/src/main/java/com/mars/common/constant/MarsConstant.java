package com.mars.common.constant;

import com.mars.server.http.constant.ReqMethod;

/**
 * 框架常量
 */
public class MarsConstant {

    /**
     * 所有数据源对象
     */
    public static final String DATA_SOURCE_MAP = "druidDataSourceMap";
    /**
     * 所有的easyAfter类信息
     */
    public static final String MARS_AFTERS = "marsAfters";
    /**
     * 记录是否已经调用完createBean方法了
     */
    public static final String HAS_START="hasStart";
    /**
     * 记录服务是否已经启动了
     */
    public static final String HAS_SERVER_START ="hasServerStart";
    /**
     * 记录是否已经启动过单测了
     */
    public static final String HAS_TEST="hasTest";
    /**
     * 所有的MarsApi类信息
     */
    public static final String CONTROLLERS = "contorllers";
    /**
     * 所有的marsBeans类信息
     */
    public static final String MARS_BEANS = "marsBeans";
    /**
     * 所有的interceptors类信息
     */
    public static final String INTERCEPTORS = "interceptors";
    /**
     * 所有的interceptors对象
     */
    public static final String INTERCEPTOR_OBJECTS = "interceptorsObjects";
    /**
     * 所有的easyDao类信息
     */
    public static final String MARS_DAOS = "marsDaos";

    /**
     * 所有的MarsApi对象
     */
    public static final String CONTROLLER_OBJECTS = "controlObjects";

    /**
     * 所有的bean对象，包括 marsBean和marsDao
     */
    public static final String MARS_BEAN_OBJECTS = "marsBeanObjects";

    /**
     * 所有加了MarsTimer注解的方法
     */
    public static final String MARS_TIMER_OBJECTS = "marsTimerObjects";

    /**
     * 方法返回值为void的标记
     */
    public static final String VOID = "void405cb55d6781877e9e930aa8e046098b";

    /**
     * 扫描到的所有类
     */
    public static final String SCAN_ALL_CLASS = "scanAllClass";

    /**
     * 请求的文件参数的key
     */
    public static final String REQUEST_FILE = "files";

    /**
     * 默认数据源储存在内存中的key
     */
    public static final String DEFAULT_DATASOURCE_NAME = "defaultDataSource";

    /**
     * config存放在内存中的key
     */
    public static final String CONFIG_CACHE_KEY = "config";

    /**
     * 参数字符编码
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 用来判断当前请求是否是一次预判
     */
    public static final String OPTIONS = "OPTIONS";

    /**
     * 请求内容类型
     */
    public static final String CONTENT_TYPE  = "Content-Type";

    /**
     * 请求内容类型
     */
    public static final String CONTENT_TYPE_LOW  = "Content-type";

    /**
     * 内容长度
     */
    public static final String CONTENT_LENGTH = "Content-Length";

    /**
     * 内容长度
     */
    public static final String CONTENT_LENGTH_LOW = "Content-length";

    /**
     * 内容描述
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    /**
     * 所有的请求方式
     */
    public static final ReqMethod[] REQ_METHODS = new ReqMethod[]{ReqMethod.GET, ReqMethod.POST, ReqMethod.DELETE, ReqMethod.PUT};
}
