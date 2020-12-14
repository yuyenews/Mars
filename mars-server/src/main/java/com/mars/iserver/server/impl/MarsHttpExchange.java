package com.mars.iserver.server.impl;

import com.mars.common.annotation.enums.ReqMethod;
import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.mars.common.util.StringUtil;
import com.mars.iserver.server.MarsServerHandler;
import com.mars.iserver.server.model.MarsHttpExchangeModel;
import com.mars.iserver.server.model.RequestURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;

/**
 * 请求解析器
 */
public class MarsHttpExchange extends MarsHttpExchangeModel  {

    private Logger log = LoggerFactory.getLogger(MarsHttpExchange.class);

    /**
     * 每次从通道读多少字节
     */
    private static int readSize;

    /**
     * 回车换行符
     */
    private static String carriageReturn = "\r\n";

    /**
     * 头结束标识
     */
    private static String headEnd = "\r\n\r\n";

    /**
     * 冒号分割符
     */
    private static String separator = ":";

    /**
     * 响应的基础信息
     */
    public static final String BASIC_RESPONSE = "HTTP/1.1 {statusCode} OK" + carriageReturn +
            "Vary: Accept-Encoding";

    /**
     * 初始化
     */
    public MarsHttpExchange(){
        super();
        readSize = MarsConfiguration.getConfig().readSize();
    }

    /**
     * 解析与处理请求
     */
    public void handleSelectKey() {
        /*
         * 一开始需要先读取请求头，所以这里要设置小一点，防止读出过多的数据
         * 为什么设置为800字节，因为经过测试，一次很常规的请求头部大小基本在300 - 600字节左右
         * get比较特别，因为地址栏会带参数，但是get一般用来查数据，所以传参不会特别大
         */
        ByteBuffer readBuffer = ByteBuffer.allocate(800);
        readBuffer.clear();

        socketChannel = (SocketChannel) selectionKey.channel();
        try {
            /* 用来储存从socketChannel读出来的数据 */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            /* 是否已经读完head了 */
            boolean readHead = false;
            /* head的长度，用来计算body长度 */
            int headLength = 0;
            /* 内容长度 */
            long contentLength = -1;

            /* 开始读数据 */
            while (socketChannel.read(readBuffer) > -1) {
                /* 获取请求报文 */
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.limit()];
                readBuffer.get(bytes);
                readBuffer.clear();

                /* 将本次读取到的数据追加到输出流 */
                outputStream.write(bytes);

                if(!readHead){
                    String headStr = new String(outputStream.toByteArray());
                    /* 判断是否已经把头读完了，如果出现了连续的两个换行，则代表头已经读完了 */
                    if(headStr.indexOf(headEnd) < 0){
                        continue;
                    }

                    /* 解析头并获取头的长度 */
                    headLength = parseHeader(headStr);
                    readHead = true;

                    /*
                     * 如果头读完了，并且此次请求是GET，则停止，
                     * 因为GET没有Content-Length如果不停止会死循环
                     * 而且GET的信息都在头里，没有body，不停止也没意义
                     */
                    if(requestMethod.toUpperCase().equals(ReqMethod.GET.toString())){
                        break;
                    }

                    /* 从head获取到Content-Length */
                    contentLength = getRequestContentLength();
                    if(contentLength < 0){
                        /*
                         * 能进入到这里，就说明头肯定读完了，
                         * 头读完了就说明Content-Length必定有值，因为上面对GET做了break处理，而其他请求方式必定有值
                         * 所以如果没值的话就不正常了，需要停止掉，防止死循环
                         */
                        break;
                    }

                    /*
                     * 当请求头读完了以后，并且本次请求不是get
                     * 则加大每次读取的量，提高速度来继续读body
                     */
                    readBuffer = ByteBuffer.allocate(readSize);
                    readBuffer.clear();
                } else {
                    /* 判断已经读取的body长度是否等于Content-Length，如果条件满足则说明读取完成 */
                    int streamLength = outputStream.size();
                    if((streamLength - headLength) >= contentLength){
                        break;
                    }
                }
            }

            /* 从报文中获取body */
            getBody(outputStream, headLength);

            /* 执行handler */
            MarsServerHandler marsServerHandler = new MarsServerHandler();
            marsServerHandler.request(this);

            /* 响应数据 */
            responseData();

        } catch (Exception e) {
            log.error("处理请求异常异常", e);
            errorResponseText(e);
        } finally {
            try {
                socketChannel.close();
                selectionKey.cancel();
                selector.wakeup();
            } catch (Exception e){
            }
        }
    }

    /**
     * 读取请求头
     * @throws Exception
     */
    private int parseHeader(String headStr) throws Exception {
        if(StringUtil.isNull(headStr)){
            return 0;
        }
        int index = headStr.indexOf(headEnd);
        if(index > -1){
            headStr = headStr.substring(0, index);
        }

        String[] headers = headStr.split(carriageReturn);
        for(int i=0;i<headers.length;i++){
            String head = headers[i];
            if(i == 0){
                /* 读取第一行 */
                readFirstLine(head);
                continue;
            }

            if(StringUtil.isNull(head)){
                continue;
            }

            /* 读取头信息 */
            String[] header = head.split(separator);
            if (header.length < 2) {
                continue;
            }
            setRequestHeader(header[0].trim(), header[1].trim());
        }

        return (headStr + headEnd).getBytes(MarsConstant.ENCODING).length;
    }

    /**
     * 解析第一行
     * @param firstLine
     */
    private void readFirstLine(String firstLine){
        String[] parts = firstLine.split(" ");

        /*
         * 请求头的第一行必须由三部分构成，分别为 METHOD PATH VERSION
         * 比如：GET /index.html HTTP/1.1
         */
        if (parts.length < 3) {
            return;
        }

        /* 解析开头的三个信息(METHOD PATH VERSION) */
        requestMethod = parts[0];
        requestURI = new RequestURI(parts[1]);
        httpVersion = parts[2];
    }

    /**
     * 从报文中获取body
     * @param outputStream
     * @throws Exception
     */
    private void getBody(ByteArrayOutputStream outputStream, int headLen) throws Exception {
        if (outputStream == null || outputStream.size() < 1) {
            return;
        }
        requestBody = new ByteArrayInputStream(outputStream.toByteArray());
        /* 跳过head，剩下的就是body */
        requestBody.skip(headLen);
    }

    /**
     * 执行响应操作
     * @throws IOException
     */
    private void responseData() throws IOException {
        if(responseBody != null){
            /* 只要响应流不为空，就按文件下载处理 */
            responseFile();
        } else {
            responseText(null);
        }
    }

    /**
     * 响应文件流
     */
    private void responseFile() throws IOException {
        /* 加载响应头 */
        setResponseHeader(MarsConstant.CONTENT_TYPE, "application/octet-stream");
        StringBuffer buffer = getCommonResponse(responseBody.length);

        /* 转成ByteBuffer */
        byte[] bytes = buffer.toString().getBytes(MarsConstant.ENCODING);

        /* 加载要响应的数据 */
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + responseBody.length);
        byteBuffer.put(bytes);
        byteBuffer.put(responseBody);

        byteBuffer.flip();

        /* 开始响应 */
        while (byteBuffer.hasRemaining()){
            socketChannel.write(byteBuffer);
        }
    }

    /**
     * 响应字符串
     *
     * @return
     */
    private void responseText(String text) throws IOException {
        if(text == null){
            text = sendText;
        }
        if(text.equals(MarsConstant.VOID)){
            return;
        }
        /* 加载响应头 */
        StringBuffer buffer = getCommonResponse(text.getBytes().length);

        /* 加载要响应的数据 */
        buffer.append(text);

        /* 转成ByteBuffer */
        byte[] bytes = buffer.toString().getBytes(MarsConstant.ENCODING);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);

        byteBuffer.flip();

        /* 开始响应 */
        while (byteBuffer.hasRemaining()){
            socketChannel.write(byteBuffer);
        }
    }

    /**
     * 异常的时候给前端一个响应
     * @param e
     */
    private void errorResponseText(Exception e){
        try {
            setResponseHeader(MarsConstant.CONTENT_TYPE, "text/json;charset="+MarsConstant.ENCODING);
            responseText("处理请求异常异常" + e.getMessage());
        } catch (Exception ex){
        }
    }

    /**
     * 获取公共的返回信息
     * @return
     */
    private StringBuffer getCommonResponse(int length){
        StringBuffer buffer = new StringBuffer();

        /* 加载初始化头 */
        buffer.append(BASIC_RESPONSE.replace("{statusCode}", String.valueOf(statusCode)));
        buffer.append(carriageReturn);
        buffer.append(MarsConstant.CONTENT_LENGTH + ": " + length);
        buffer.append(carriageReturn);

        /* 加载自定义头 */
        for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()){
            List<String> values = entry.getValue();
            if(values == null || values.size() < 1){
                continue;
            }
            StringBuffer valueStr = new StringBuffer();
            for(int i=0;i<values.size();i++){
                String value = values.get(i);
                if(i > 0){
                    valueStr.append(",");
                }
                valueStr.append(value);
            }

            buffer.append(entry.getKey() + ":" + valueStr.toString());
            buffer.append(carriageReturn);
        }
        buffer.append(carriageReturn);
        return buffer;
    }

    /**
     * 获取本次的请求类型
     * @return
     */
    public String getContentType(){
        List<String> headList = requestHeaders.get(MarsConstant.CONTENT_TYPE);
        if(headList == null || headList.size() < 1){
            headList = requestHeaders.get(MarsConstant.CONTENT_TYPE_LOW);
        }

        if(headList == null || headList.size() < 1){
            return null;
        }
        return headList.get(0);
    }

    /**
     * 获取请求长度
     * @return
     */
    public long getRequestContentLength(){
        List<String> lengthList = requestHeaders.get(MarsConstant.CONTENT_LENGTH);
        if(lengthList == null || lengthList.size() < 1){
            lengthList = requestHeaders.get(MarsConstant.CONTENT_LENGTH_LOW);
        }

        if(lengthList == null || lengthList.size() < 1){
            return -1;
        }

        return Long.parseLong(lengthList.get(0));
    }
}
