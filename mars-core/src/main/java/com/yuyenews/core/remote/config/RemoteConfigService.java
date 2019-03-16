package com.yuyenews.core.remote.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.logger.MarsLog4jUtil;
import com.yuyenews.core.logger.MarsLogger;
import com.yuyenews.core.util.ConfigUtil;

import java.lang.reflect.Method;

/**
 * 远程配置服务
 */
public class RemoteConfigService {


    private static MarsLogger marsLogger = MarsLogger.getLogger(RemoteConfigService.class);

    /**
     * 重新加载配置
     * @param config
     *
     * @return 加载结果
     */
    public static String reloadConfig(Object config){
        try {

            marsLogger.warn("功能开发中");

            return "ok";
        } catch (Exception e){
            marsLogger.error("接收配置中心的通知失败",e);
            return "error";
        }
    }

}
