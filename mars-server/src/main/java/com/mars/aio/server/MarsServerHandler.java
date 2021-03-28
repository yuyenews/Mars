package com.mars.aio.server;

import com.mars.aio.server.impl.MarsHttpExchange;

/**
 * 联络器
 */
public interface MarsServerHandler {

    /**
     * 开始联络业务逻辑为本次请求做服务
     * @param exchange
     */
    void request(MarsHttpExchange exchange);
}
