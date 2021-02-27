package com.mars.iserver.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.MarsConfiguration;
import com.mars.iserver.server.MarsRequest;
import com.mars.iserver.server.MarsServer;
import com.mars.iserver.server.threadpool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 默认服务（采用NIO）
 */
public class MarsDefaultServer implements MarsServer {

    private Logger log = LoggerFactory.getLogger(MarsDefaultServer.class);

    /**
     * 开启服务
     *
     * @param portNumber
     */
    @Override
    public void start(int portNumber) {
        try {
            /* 获取配置的最大连接数 */
            int backLog = MarsConfiguration.getConfig().threadPoolConfig().getBackLog();

            /* 开始监听端口 */
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(portNumber), backLog);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            /* 标识服务是否已经启动 */
            MarsSpace.getEasySpace().setAttr(MarsConstant.HAS_SERVER_START, "yes");
            log.info("启动成功");

            /* 开始监听 */
            doMonitor(selector);
        } catch (Exception e) {
            log.error("NIO发生异常", e);
        }
    }

    /**
     * 开始注册并监听
     *
     * @param selector
     * @throws Exception
     */
    private void doMonitor(Selector selector) throws Exception {
        while (true) {
            int eventCountTriggered = selector.select();
            if (eventCountTriggered == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if(selectionKeys == null || selectionKeys.size() < 1){
                continue;
            }

            /* 计数器，用来记录当前的key是否都已经处理完了 */
            CountDownLatch countDownLatch = new CountDownLatch(selectionKeys.size());

            /* 处理获取到的这一批key */
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();

                if(!selectionKey.isValid()){
                    continue;
                }

                MarsRequest marsRequest = new MarsRequest();
                marsRequest.setCountDownLatch(countDownLatch);
                marsRequest.setSelectionKey(selectionKey);
                marsRequest.setSelector(selector);

                ThreadPool.getThreadPoolExecutor().execute(marsRequest);
            }

            /* 等待这一批key处理完了，再进行下一次循环 */
            countDownLatch.await();

            /* 唤醒selector */
            selector.wakeup();
        }
    }
}
