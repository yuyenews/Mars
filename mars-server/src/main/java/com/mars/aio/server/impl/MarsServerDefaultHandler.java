package com.mars.aio.server.impl;

import com.mars.aio.execute.RequestExecute;
import com.mars.aio.util.ResponseUtil;
import com.mars.aio.server.request.HttpMarsRequest;
import com.mars.aio.server.request.HttpMarsResponse;
import com.mars.aio.server.request.impl.HttpMarsDefaultRequest;
import com.mars.aio.server.request.impl.HttpMarsDefaultResponse;
import com.mars.server.tcp.http.handler.ext.HttpRequestHandler;
import com.mars.server.tcp.http.request.MartianHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认
 */
public class MarsServerDefaultHandler implements HttpRequestHandler {

    private Logger log = LoggerFactory.getLogger(MarsServerDefaultHandler.class);

    /**
     * 开始联络业务逻辑为本次请求做服务
     * @param martianHttpRequest
     */
    @Override
    public void request(MartianHttpRequest martianHttpRequest) {
        /* 组装httpRequest对象 */
        HttpMarsRequest request = new HttpMarsDefaultRequest(martianHttpRequest);

        /* 组装httpResponse对象 */
        HttpMarsResponse response = new HttpMarsDefaultResponse(martianHttpRequest);

        try {
            RequestExecute.execute(request, response);
        } catch (Exception e) {
            log.error("处理请求失败!", e);
            ResponseUtil.sendServerError(response,"处理请求发生错误"+e.getMessage());
        }
    }
}
