package com.mars.aio.par;

import com.mars.aio.server.request.HttpMarsRequest;

/**
 * 初始化request
 *
 * @author yuye
 */
public interface InitRequest {



    /**
     * 从请求中提取出所有的参数，并放置到HttpMarsRequest中
     * @param marsRequest mars请求
     * @return 加工后的mars请求
     * @throws Exception 异常
     */
    HttpMarsRequest getHttpMarsRequest(HttpMarsRequest marsRequest) throws Exception;
}
