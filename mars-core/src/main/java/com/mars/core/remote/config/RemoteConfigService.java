package com.mars.core.remote.config;

import com.mars.core.logger.MarsLogger;

/**
 * 远程配置服务
 */
public class RemoteConfigService {


    private static MarsLogger marsLogger = MarsLogger.getLogger(RemoteConfigService.class);

    /**
     * 重新加载配置
     * @param config 配置文件
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
