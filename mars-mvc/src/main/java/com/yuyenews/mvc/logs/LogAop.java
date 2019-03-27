package com.yuyenews.mvc.logs;

import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.logger.MarsLogger;
import com.yuyenews.server.server.request.HttpRequest;

import java.util.Map;

/**
 * controller方法打印日志
 */
public class LogAop {

    private MarsLogger logger = MarsLogger.getLogger(LogAop.class);

    private Class cls;
    private String methodName;

    public LogAop(Class cls,String methodName){
        this.cls = cls;
        this.methodName = methodName;
    }

    /**
     * controller方法开始执行
     * @param args
     */
    public void startMethod(Object[] args) {
        Object obj = args[0];
        if(obj != null && obj instanceof HttpRequest){
            HttpRequest request = (HttpRequest)obj;
            Map<String, Object> params = request.getParemeters();

            StringBuffer buffer = new StringBuffer();
            buffer.append("开始执行");
            buffer.append(cls.getName());
            buffer.append("->");
            buffer.append(methodName);
            buffer.append(",参数:[");
            buffer.append(JSONObject.toJSONString(params));
            buffer.append("]");

            logger.info(buffer.toString());
        }
    }

    /**
     * controller方法结束执行
     * @param args
     * @param result
     */
    public void endMethod(Object[] args,Object result) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("执行结束");
        buffer.append(cls.getName());
        buffer.append("->");
        buffer.append(methodName);
        buffer.append(",返回数据:[");
        buffer.append(JSONObject.toJSONString(result));
        buffer.append("]");

        logger.info(buffer.toString());
    }

    /**
     * controller方法出异常
     * @param e
     */
    public void exp(Throwable e){
        StringBuffer buffer = new StringBuffer();
        buffer.append("执行异常");
        buffer.append(cls.getName());
        buffer.append("->");
        buffer.append(methodName);
        buffer.append(",异常信息：");

        logger.error(buffer.toString(),e);
    }
}
