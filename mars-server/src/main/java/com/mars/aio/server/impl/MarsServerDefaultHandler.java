package com.mars.aio.server.impl;

import com.mars.aio.execute.RequestExecute;
import com.mars.server.http.handler.MartianServerHandler;
import com.mars.aio.util.ResponseUtil;
import com.mars.server.http.request.MartianHttpExchange;
import com.mars.aio.server.request.HttpMarsRequest;
import com.mars.aio.server.request.HttpMarsResponse;
import com.mars.aio.server.request.impl.HttpMarsDefaultRequest;
import com.mars.aio.server.request.impl.HttpMarsDefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认
 */
public class MarsServerDefaultHandler implements MartianServerHandler {

    private Logger log = LoggerFactory.getLogger(MarsServerDefaultHandler.class);

    /**
     * 开始联络业务逻辑为本次请求做服务
     * @param martianHttpExchange
     */
    @Override
    public void request(MartianHttpExchange martianHttpExchange) {
        /* 组装httpRequest对象 */
        HttpMarsRequest request = new HttpMarsDefaultRequest(martianHttpExchange);;

        /* 组装httpResponse对象 */
        HttpMarsResponse response = new HttpMarsDefaultResponse(martianHttpExchange);

        try {
            RequestExecute.execute(request, response);
        } catch (Exception e) {
            log.error("处理请求失败!", e);
            ResponseUtil.sendServerError(response,"处理请求发生错误"+e.getMessage());
        }
    }
}
