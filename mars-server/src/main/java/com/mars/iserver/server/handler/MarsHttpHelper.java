package com.mars.iserver.server.handler;

import com.mars.common.annotation.enums.ReqMethod;
import com.mars.common.base.config.model.RequestConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.mars.common.util.StringUtil;
import com.mars.iserver.constant.HttpConstant;
import com.mars.iserver.server.MarsServerHandler;
import com.mars.iserver.server.factory.MarsServerHandlerFactory;
import com.mars.iserver.server.impl.MarsHttpExchange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Http注册，读取，写入 帮助类
 */
public class MarsHttpHelper {

    /**
     * 每次从通道读多少字节
     */
    private static int readSize;

    /**
     * 读取超时时间
     */
    private static int readTimeout;

    /**
     * 请求配置
     */
    private static RequestConfig requestConfig = MarsConfiguration.getConfig().requestConfig();

    /**
     * 初始化
     */
    private static void init() {
        readSize = requestConfig.getReadSize();
        readTimeout = requestConfig.getReadTimeout();
    }

    /**
     * 读取数据并创建MarsHttpExchange对象
     */
    public static void read(AsynchronousSocketChannel channel, MarsHttpExchange marsHttpExchange) throws Exception {
        if (readSize <= 0 || readTimeout <= 0) {
            init();
        }
        /* 一开始需要先读取请求头，所以这里要设置小一点，防止读出过多的数据*/
        ByteBuffer readBuffer = ByteBuffer.allocate(800);
        readBuffer.clear();

        /* 用来储存从socketChannel读出来的数据 */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        /* 是否已经读完head了 */
        boolean readHead = false;
        /* head的长度，用来计算body长度 */
        int headLength = 0;
        /* 内容长度 */
        long contentLength = Long.MAX_VALUE;
        /* 开始读取时间 */
        long start = System.currentTimeMillis();

        /* 开始读数据 */
        while (channel.read(readBuffer).get() > -1) {
            /* 计算是否超时 */
            isReadTimeout(start);

            /* 获取请求报文 */
            byte[] bytes = getReadData(readBuffer);
            /* 将本次读取到的数据追加到输出流 */
            outputStream.write(bytes);

            if (!readHead) {
                String headStr = new String(outputStream.toByteArray());
                /* 判断是否已经把头读完了，如果出现了连续的两个换行，则代表头已经读完了 */
                int headEndIndex = headStr.indexOf(HttpConstant.HEAD_END);
                if (headEndIndex < 0) {
                    continue;
                }

                /* 解析头并获取头的长度 */
                headLength = parseHeader(headStr, headEndIndex, marsHttpExchange);
                readHead = true;
                /* 如果头读完了，并且此次请求是GET，则停止 */
                if (marsHttpExchange.getRequestMethod().toUpperCase().equals(ReqMethod.GET.toString())) {
                    break;
                }

                /* 从head获取到Content-Length */
                contentLength = marsHttpExchange.getRequestContentLength();
                if (contentLength < 0) {
                    break;
                }
            }

            /* 判断已经读取的body长度是否等于Content-Length，如果条件满足则说明读取完成 */
            int streamLength = outputStream.size();
            if ((streamLength - headLength) >= contentLength) {
                break;
            }

            /* 当请求头读完了以后，就加大每次读取大小 加快速度 */
            readBuffer = ByteBuffer.allocate(readSize);
            readBuffer.clear();
        }

        /* 从报文中获取body */
        getBody(outputStream, headLength, marsHttpExchange);
    }

    /**
     * 读取数据
     *
     * @param readBuffer
     * @return
     */
    private static byte[] getReadData(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.limit()];
        readBuffer.get(bytes);
        readBuffer.clear();
        return bytes;
    }

    /**
     * 是否超时了
     *
     * @param start
     * @throws Exception
     */
    private static void isReadTimeout(long start) throws Exception {
        long end = System.currentTimeMillis();
        if ((end - start) > readTimeout) {
            throw new Exception("读取请求数据超时");
        }
    }

    /**
     * 读取请求头
     *
     * @throws Exception
     */
    private static int parseHeader(String headStr, int headEndIndex, MarsHttpExchange marsHttpExchange) throws Exception {
        headStr = headStr.substring(0, headEndIndex);

        String[] headers = headStr.split(HttpConstant.CARRIAGE_RETURN);
        for (int i = 0; i < headers.length; i++) {
            String head = headers[i];
            if (i == 0) {
                /* 读取第一行 */
                readFirstLine(head, marsHttpExchange);
                continue;
            }

            if (StringUtil.isNull(head)) {
                continue;
            }

            /* 读取头信息 */
            String[] header = head.split(HttpConstant.SEPARATOR);
            if (header.length < 2) {
                continue;
            }
            marsHttpExchange.setRequestHeader(header[0].trim(), header[1].trim());
        }

        return (headStr + HttpConstant.HEAD_END).getBytes(MarsConstant.ENCODING).length;
    }

    /**
     * 解析第一行
     *
     * @param firstLine
     */
    private static void readFirstLine(String firstLine, MarsHttpExchange marsHttpExchange) {
        String[] parts = firstLine.split("\\s+");

        /*
         * 请求头的第一行必须由三部分构成，分别为 METHOD PATH VERSION
         * 比如：GET /index.html HTTP/1.1
         */
        if (parts.length < 3) {
            return;
        }
        /* 解析开头的三个信息(METHOD PATH VERSION) */
        marsHttpExchange.setRequestMethod(parts[0]);
        marsHttpExchange.setRequestURI(parts[1]);
        marsHttpExchange.setHttpVersion(parts[2]);
    }

    /**
     * 从报文中获取body
     *
     * @param outputStream
     * @throws Exception
     */
    private static void getBody(ByteArrayOutputStream outputStream, int headLen, MarsHttpExchange marsHttpExchange) throws Exception {
        if (outputStream == null || outputStream.size() < 1) {
            return;
        }
        ByteArrayInputStream requestBody = new ByteArrayInputStream(outputStream.toByteArray());
        /* 跳过head，剩下的就是body */
        requestBody.skip(headLen);

        marsHttpExchange.setRequestBody(requestBody);
    }

    /**
     * 响应
     *
     */
    public static void write(MarsHttpExchange marsHttpExchange) throws Exception {
        /* 执行handler */
        MarsServerHandler marsServerHandler = MarsServerHandlerFactory.getMarsServerHandler();
        marsServerHandler.request(marsHttpExchange);

        /* 响应数据 */
        marsHttpExchange.responseData();
    }
}
