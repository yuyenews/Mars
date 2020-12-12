package com.mars.iserver.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.common.util.MarsConfiguration;
import com.mars.common.util.StringUtil;
import com.mars.iserver.constant.ParamTypeConstant;
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
     * 请求数据大小
     */
    private static int requestSize;

    /**
     * 回车换行符
     */
    private static String carriageReturn = "\r\n";

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
        requestSize = MarsConfiguration.getConfig().readSize();
    }

    /**
     * 解析与处理请求
     */
    public void handleSelectKey() {
        ByteBuffer readBuffer = ByteBuffer.allocate(requestSize);
        readBuffer.clear();

        socketChannel = (SocketChannel) selectionKey.channel();
        try {
            // TODO
            while (socketChannel.read(readBuffer) > 0) {}

            /* 获取请求报文 */
            readBuffer.flip();
            byte[] bytes = new byte[readBuffer.limit()];
            readBuffer.get(bytes);

            /* 解析请求 */
            parseRequest(bytes);

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
                selectionKey.cancel();
                selector.wakeup();
                socketChannel.close();
            } catch (Exception e){
            }
        }
    }

    /**
     * 解析请求
     * @param bytes
     * @throws Exception
     */
    private void parseRequest(byte[] bytes) throws Exception {
        if (bytes == null || bytes.length < 1) {
            return;
        }

        InputStream body = new ByteArrayInputStream(bytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(body, MarsConstant.ENCODING));

        boolean isFirst = true;
        boolean isFirstContent = true;
        StringBuffer buffer = null;

        String line = null;
        while ((line = br.readLine()) != null) {
            if(isFirst){
                /* 读取第一行 */
                readFirstLine(line);
                isFirst = false;
                continue;
            }

            /* 判断头读完了没 */
            if (StringUtil.isNull(line) && buffer == null){
                /* 遇到空行就说明从下一行开始是内容了 */
                buffer = new StringBuffer();

                boolean isContinue = isFormData();
                if(isContinue){
                    /* 如果formData就不需要读内容了，直接将整个请求体返回即可 */
                    break;
                } else {
                    continue;
                }
            }

            if(buffer != null){
                /* 读取内容 */
                if(!isFirstContent){
                    buffer.append(carriageReturn);
                }
                buffer.append(line);
                isFirstContent = false;
            } else {
                /* 读取头信息 */
                String[] header = line.split(separator);
                if (header.length < 2) {
                    continue;
                }
                setRequestHeader(header[0].trim(), header[1].trim());
            }
        }

        /* 保存本次的请求体 */
        if(isFormData()){
            requestBody = new ByteArrayInputStream(bytes);
        } else if(buffer != null){
            requestBody = new ByteArrayInputStream(buffer.toString().getBytes(MarsConstant.ENCODING));
        }
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
     * 执行响应操作
     * @throws IOException
     */
    private void responseData() throws IOException {
        if(responseBody == null){
            responseText(null);
        } else {
            responseFile();
        }
    }

    /**
     * 响应文件流
     */
    private void responseFile() throws IOException {
        /* 加载响应头 */
        setResponseHeader(MarsConstant.CONTENT_TYPE, "application/octet-stream");

        /* 加载响应头 */
        StringBuffer buffer = getCommonResponse(responseBody.length);
        buffer.append(carriageReturn);

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
        buffer.append(carriageReturn);
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

        return buffer;
    }

    /**
     * 是否是formData
     * @return
     */
    private boolean isFormData(){
        String contentType = getContentType();
        if(ParamTypeConstant.isFormData(contentType)){
            return true;
        }
        return false;
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
}
