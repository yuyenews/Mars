package com.mars.mvc.util;

import com.mars.common.constant.MarsConstant;
import com.mars.common.util.JSONUtil;
import com.mars.common.util.StringUtil;
import com.mars.core.enums.DataType;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.iserver.par.base.BaseParamAndResult;

import java.lang.reflect.Method;

/**
 * 参数和返回，实现类
 */
public class ParamAndResult implements BaseParamAndResult {

    /**
     * 获取参数
     * @param method 方法
     * @param request 请求
     * @param response 响应
     * @return 参数
     * @throws Exception 异常
     */
    @Override
    public Object[] getParam(Method method, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
        return BuildParams.builder(method,request,response);
    }


    /**
     * 返回数据
     * @param response 响应
     * @param resultObj 返回的数据
     * @throws Exception 异常
     */
    @Override
    public void result(HttpMarsResponse response, Object resultObj) throws Exception {
        if(StringUtil.isNull(resultObj)){
            resultObj = "{}";
        } else if(resultObj.toString().equals(MarsConstant.VOID)) {
            return;
        } else if(!isNotObject(resultObj)){
            resultObj = JSONUtil.toJSONString(resultObj);
        }

        response.send(String.valueOf(resultObj));
    }

    /**
     * 判断是否是对象
     * @param result
     * @return
     */
    private boolean isNotObject(Object result){
        if(result == null){
            return true;
        }
        String fieldTypeName = result.getClass().getSimpleName().toUpperCase();
        switch (fieldTypeName){
            case DataType.INT:
            case DataType.INTEGER:
            case DataType.BYTE:
            case DataType.STRING:
            case DataType.CHAR:
            case DataType.CHARACTER:
            case DataType.DOUBLE:
            case DataType.FLOAT:
            case DataType.LONG:
            case DataType.SHORT:
            case DataType.BOOLEAN:
            case DataType.DATE:
                return true;
        }
        return false;
    }
}
