package com.mars.core.logger;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;

public abstract class MarsLogger {


    public abstract void info(String info);

    public abstract void warn(String info);

    public abstract void warn(String info,Throwable e);

    public abstract void error(String info);

    public abstract void error(String info,Throwable e);

    /**
     * 获取MarsLogger 对象
     * @param cls
     * @return
     */
    public static MarsLogger getLogger(Class cls){
        boolean str = hasLog4j();
        if(str){
            return new LoggerSlf4j(cls);
        }
        return new LoggerLog4j(cls);
    }

    /**
     * 检查用户是否使用了log4j
     * @return 布尔值
     */
    private static boolean hasLog4j(){
        JSONObject config = ConfigUtil.getConfig();
        if(config == null){
            return true;
        }
        Object obj = config.get("logFile");
        return obj == null;
    }
}
