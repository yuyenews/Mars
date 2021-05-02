package com.martian.starter.load;

import com.martian.cache.MartianConfigCache;
import com.martian.config.MartianConfig;
import com.martian.config.model.CrossDomainConfig;
import com.martian.config.model.FileUploadConfig;
import com.martian.config.model.RequestConfig;
import io.magician.Magician;
import io.magician.common.event.EventGroup;
import io.magician.tcp.TCPServerConfig;
import io.magician.tcp.codec.impl.http.request.MagicianRequest;

import java.util.concurrent.Executors;

/**
 * 加载服务
 */
public class LoadServer {

    /**
     * 开始加载
     * @throws Exception
     */
    public static void load() throws Exception {

        /* 获取配置 */
        MartianConfig martianConfig = MartianConfigCache.getMartianConfig();
        FileUploadConfig fileUploadConfig = martianConfig.fileUploadConfig();
        RequestConfig requestConfig = martianConfig.requestConfig();
        CrossDomainConfig crossDomainConfig = martianConfig.crossDomainConfig();

        /* 创建TCPServer配置 */
        TCPServerConfig tcpServerConfig = new TCPServerConfig();
        tcpServerConfig.setFileSizeMax(fileUploadConfig.getFileSizeMax());
        tcpServerConfig.setSizeMax(fileUploadConfig.getSizeMax());
        tcpServerConfig.setReadSize(requestConfig.getReadSize());

        EventGroup ioEventGroup = new EventGroup(1, Executors.newCachedThreadPool());
        EventGroup workerEventGroup = martianConfig.workerEventGroup();

        /* 创建服务 */
        Magician.createTCPServer(ioEventGroup, workerEventGroup)
                .config(tcpServerConfig)
                .handler("/", req -> {
                    MagicianRequest magicianRequest = (MagicianRequest)req;
                    magicianRequest.getResponse().setResponseHeader("Access-Control-Allow-Origin", crossDomainConfig.getOrigin());
                    magicianRequest.getResponse().setResponseHeader("Access-Control-Allow-Methods", crossDomainConfig.getMethods());
                    magicianRequest.getResponse().setResponseHeader("Access-Control-Max-Age", crossDomainConfig.getMaxAge());
                    magicianRequest.getResponse().setResponseHeader("Access-Control-Allow-Headers", crossDomainConfig.getHeaders());
                    magicianRequest.getResponse().setResponseHeader("Access-Control-Allow-Credentials", crossDomainConfig.getCredentials());

                    LoadWeb.getMagicianWeb().request(magicianRequest);
                }).bind(martianConfig.port(), 1000);
    }
}
