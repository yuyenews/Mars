package com.yuyenews.core.constant;

/**
 * 框架常量
 */
public class EasyConstant {

    /**
     * druid数据源加载类
      */
    public static final String DRUID_DATA_SOURCE = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * 配置文件中数据源节点的属性名
     */
    public static final String DATA_SOURCE = "dataSource";
    /**
     * 本地配置文件
     */
    public static final String CONFIG_PATH = "/mars.yml";
    /**
     * 所有的easyAfter类信息
     */
    public static final String EASYAFTERS = "easyAfters";
    /**
     * 记录是否已经调用完createbean方法了
     */
    public static final String HAS_START="hasStart";
    /**
     * 所有的controller类信息
     */
    public static final String CONTROLLERS = "contorllers";
    /**
     * 所有的easyBeans类信息
     */
    public static final String EASYBEANS = "easyBeans";
    /**
     * 所有的interceptors类信息
     */
    public static final String INTERCEPTORS = "interceptors";
    /**
     * 所有的easyDaos类信息
     */
    public static final String EASYDAOS = "easyDaos";

    /**
     * 所有的controller对象
     */
    public static final String CONTROLLER_OBJECTS = "controlObjects";

    /**
     * 所有的bean对象，包括 easyBean和easydDao
     */
    public static final String EASYBEAN_OBJECTS = "easyBeanObjects";

    /**
     * 接受远程配置中心通知的controller
     */
    public static final String REMOTE_CONFIG_CONTROLLER = "com.yuyenews.remote.config.RemoteConfigController";

    /**
     * 读取远程配置的路径
     */
    public static final String READ_REMOTE_CONFIG = "http://${0}/getConfig";
}
