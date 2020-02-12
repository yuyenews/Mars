package com.mars.mvc.logs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String params = "";
        if(args != null && args.length > 0){
            JSONObject jsonObject = new JSONObject();
            for(Object obj : args){
                if(obj == null || obj instanceof HttpMarsResponse || obj instanceof HttpMarsRequest){
                    continue;
                }
                JSONObject param = JSONObject.parseObject(JSON.toJSONString(obj));
                jsonObject.putAll(param);
            }
            params = jsonObject.toJSONString();
        }

        StringBuffer buffer = getLogInfo("开始执行","参数",params);
        logger.info(buffer.toString());
    }

    /**
     * MarsApi方法结束执行
     * @param args
     * @param result
     */
    public void endMethod(Object[] args,Object result) {
        String resultInfo = "";
        if(result != null){
            resultInfo = JSON.toJSONString(result);
        }
        StringBuffer buffer = getLogInfo("执行结束","返回数据", resultInfo);
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

    private StringBuffer getLogInfo(String startWith, String tag, String result){
        StringBuffer buffer = new StringBuffer();
        buffer.append(startWith);
        buffer.append(cls.getName());
        buffer.append("->");
        buffer.append(methodName);
        buffer.append(",");
        buffer.append(tag);
        buffer.append("[");
        buffer.append(result);
        buffer.append("]");

        return buffer;
    }
}
