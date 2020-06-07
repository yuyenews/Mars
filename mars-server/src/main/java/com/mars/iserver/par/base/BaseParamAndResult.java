package com.mars.iserver.par.base;

import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

import java.lang.reflect.Method;

/**
 * 参数和返回
 */
public interface BaseParamAndResult {

    /**
     * 获取参数
     * @param method 方法
     * @param request 请求
     * @param response 响应
     * @return 参数
     * @throws Exception 异常
     */
    Object[] getParam(Method method, HttpMarsRequest request, HttpMarsResponse response) throws Exception;

    /**
     * 返回数据
     * @param response 响应
     * @param resultObj 返回的数据
     * @throws Exception 异常
     */
    void result(HttpMarsResponse response, Object resultObj) throws Exception;
}
