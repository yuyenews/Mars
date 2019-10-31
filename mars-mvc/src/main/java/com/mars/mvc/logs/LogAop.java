package com.mars.mvc.logs;

import com.alibaba.fastjson.JSONObject;
import com.mars.server.server.request.HttpMarsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * MarsApi方法打印日志
 */
public class LogAop {

    private Logger logger = LoggerFactory.getLogger(LogAop.class);

    private Class cls;
    private String methodName;

    public LogAop(Class cls,String methodName){
        this.cls = cls;
        this.methodName = methodName;
    }

    /**
     * MarsApi方法开始执行
     * @param args
     */
    public void startMethod(Object[] args) {
        Object obj = args[0];
        if(obj != null && obj instanceof HttpMarsRequest){
            HttpMarsRequest request = (HttpMarsRequest)obj;
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
     * MarsApi方法结束执行
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
     * MarsApi方法出异常
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
