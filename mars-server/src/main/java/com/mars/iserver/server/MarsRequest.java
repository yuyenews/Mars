package com.mars.iserver.server;

import com.mars.iserver.server.helper.MarsHttpHelper;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.CountDownLatch;

/**
 * 执行请求 和 完成响应 的线程
 */
public class MarsRequest implements Runnable {

    /**
     * 计数器
     */
    private CountDownLatch countDownLatch;

    /**
     * 选择器
     */
    private Selector selector;

    /**
     * Key
     */
    private SelectionKey selectionKey;

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        if (selectionKey.isAcceptable()) {
            MarsHttpHelper.acceptable(selector, selectionKey);
        } else if (selectionKey.isReadable()) {
            MarsHttpHelper.read(selector, selectionKey);
        } else if(selectionKey.isWritable()){
            MarsHttpHelper.write(selectionKey);
        }
        countDownLatch.countDown();
    }
}
