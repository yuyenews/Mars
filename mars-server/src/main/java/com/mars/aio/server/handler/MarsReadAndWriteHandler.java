package com.mars.aio.server.handler;

import com.mars.aio.constant.HttpConstant;
import com.mars.aio.server.MarsServerHandler;
import com.mars.aio.server.factory.MarsServerHandlerFactory;
import com.mars.aio.server.helper.MarsHttpHelper;
import com.mars.aio.server.impl.MarsHttpExchange;
import com.mars.common.annotation.enums.ReqMethod;
import com.mars.common.base.config.model.RequestConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.mars.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * http读写handler
 */
public class MarsReadAndWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private Logger logger = LoggerFactory.getLogger(MarsAioServerHandler.class);

    /**
     * 通道
     */
    private AsynchronousSocketChannel channel;

    /**
     * 请求对象
     */
    private MarsHttpExchange marsHttpExchange;

    /**
     * 是否已经读完head了
     */
    private boolean readHead = false;
    /**
     * head的长度，用来计算body长度
     */
    private int headLength = 0;
    /**
     * 内容长度
     */
    private long contentLength = Long.MAX_VALUE;
    /**
     * 读取到的数据缓存到这里
     */
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * 请求配置
     */
    private static RequestConfig requestConfig = MarsConfiguration.getConfig().requestConfig();

    /**
     * 构造函数
     * @param marsHttpExchange
     */
    public MarsReadAndWriteHandler(MarsHttpExchange marsHttpExchange){
        this.channel = marsHttpExchange.getSocketChannel();
        this.marsHttpExchange = marsHttpExchange;
    }

    /**
     * 读取请求数据
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            /* 如果客户端没断开，那就从通道读取数据 */
            if(result > 0){
                ByteBuffer readBuffer = read(attachment);
                if(readBuffer != null){
                    /* 如果数据没读完，就接着读 */
                    channel.read(readBuffer, requestConfig.getReadTimeout(), TimeUnit.MILLISECONDS, readBuffer, this);
                    return;
                }
            }

            channel.shutdownInput();

            /* 过滤掉非法请求 */
            if(marsHttpExchange.getRequestURI() == null
                    || marsHttpExchange.getRequestMethod() == null
                    || marsHttpExchange.getHttpVersion() == null){
                MarsHttpHelper.close(channel, false);
                return;
            }
            /* 如果数据没读完，会在第一段的if里被return掉，所以执行到这肯定是已经读完了，所以接着执行业务逻辑，并关闭通道 */
            getBody();
            write();
        } catch (Exception e){
            logger.error("读取数据异常", e);
            MarsHttpHelper.errorResponseText(e, marsHttpExchange);
            MarsHttpHelper.close(channel, true);
        }
    }

    /**
     * 读取异常处理
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error("读取数据异常", exc);
        MarsHttpHelper.errorResponseText(exc, marsHttpExchange);
        MarsHttpHelper.close(channel, true);
    }

    /**
     * 从通道读取数据
     * @param readBuffer
     * @return
     * @throws Exception
     */
    private ByteBuffer read(ByteBuffer readBuffer) throws Exception {
        /* 获取请求报文 */
        byte[] bytes = getReadData(readBuffer);
        /* 将本次读取到的数据追加到输出流 */
        outputStream.write(bytes);

        if (!readHead) {
            String headStr = new String(outputStream.toByteArray());
            /* 判断是否已经把头读完了，如果出现了连续的两个换行，则代表头已经读完了 */
            int headEndIndex = headStr.indexOf(HttpConstant.HEAD_END);
            if (headEndIndex < 0) {
                return readBuffer;
            }

            /* 解析头并获取头的长度 */
            headLength = parseHeader(headStr, headEndIndex);
            readHead = true;
            /* 如果头读完了，并且此次请求是GET，则停止 */
            if (marsHttpExchange.getRequestMethod().toUpperCase().equals(ReqMethod.GET.toString())) {
                return null;
            }

            /* 从head获取到Content-Length */
            contentLength = marsHttpExchange.getRequestContentLength();
            if (contentLength < 0) {
                return null;
            }
        }

        /* 判断已经读取的body长度是否等于Content-Length，如果条件满足则说明读取完成 */
        int streamLength = outputStream.size();
        if ((streamLength - headLength) >= contentLength) {
            return null;
        }

        /* 当请求头读完了以后，就加大每次读取大小 加快速度 */
        readBuffer = ByteBuffer.allocate(requestConfig.getReadSize());
        readBuffer.clear();

        return readBuffer;
    }

    /**
     * 读取数据
     *
     * @param readBuffer
     * @return
     */
    private byte[] getReadData(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.limit()];
        readBuffer.get(bytes);
        readBuffer.clear();
        return bytes;
    }

    /**
     * 读取请求头
     *
     * @throws Exception
     */
    private int parseHeader(String headStr, int headEndIndex) throws Exception {
        headStr = headStr.substring(0, headEndIndex);

        String[] headers = headStr.split(HttpConstant.CARRIAGE_RETURN);
        for (int i = 0; i < headers.length; i++) {
            String head = headers[i];
            if (i == 0) {
                /* 读取第一行 */
                readFirstLine(head);
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
    private void readFirstLine(String firstLine) {
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
     * @throws Exception
     */
    private void getBody() {
        if (outputStream == null || outputStream.size() < 1) {
            return;
        }
        ByteArrayInputStream requestBody = new ByteArrayInputStream(outputStream.toByteArray());
        /* 跳过head，剩下的就是body */
        requestBody.skip(headLength);

        marsHttpExchange.setRequestBody(requestBody);
    }

    /**
     * 响应
     *
     */
    public void write() throws Exception {
        /* 执行handler */
        MarsServerHandler marsServerHandler = MarsServerHandlerFactory.getMarsServerHandler();
        marsServerHandler.request(marsHttpExchange);

        /* 响应数据 */
        marsHttpExchange.responseData();
    }
}
