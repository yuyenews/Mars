package com.mars.iserver.server.impl;

import com.mars.iserver.server.MarsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class MarsDefaultServer implements MarsServer {

    private Logger log = LoggerFactory.getLogger(MarsDefaultServer.class);

    private String hostName = "localhost";

    @Override
    public void start(int portNumber) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(hostName, portNumber));

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int eventCountTriggered = selector.select();
                if (eventCountTriggered == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel2 = (ServerSocketChannel) selectionKey.channel();
                        try {
                            SocketChannel socketChannel = serverSocketChannel2.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } catch (IOException e) {
                            log.error("注册SocketChannel异常", e);
                        }
                    } else if (selectionKey.isReadable()) {
                        MarsHttpExchange marsHttpExchange = new MarsHttpExchange();
                        marsHttpExchange.setSelectionKey(selectionKey);
                        marsHttpExchange.setSelector(selector);
                        marsHttpExchange.handleSelectKey();
                    }
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            log.error("NIO发生异常", e);
        }
    }
}
