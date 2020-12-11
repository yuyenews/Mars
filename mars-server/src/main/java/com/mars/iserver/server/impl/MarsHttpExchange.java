package com.mars.iserver.server.impl;

import com.mars.common.constant.MarsConstant;
import com.mars.common.util.StringUtil;
import com.mars.iserver.server.MarsServerHandler;
import com.mars.iserver.server.model.HttpHeaders;
import com.mars.iserver.server.model.MarsHttpExchangeModel;
import com.mars.iserver.server.model.RequestURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;

public class MarsHttpExchange extends MarsHttpExchangeModel  {

    private Logger log = LoggerFactory.getLogger(MarsHttpExchange.class);

    /**
     * 每次读取大小
     */
    private static final ByteBuffer READ_BUFFER = ByteBuffer.allocate(1024 * 4);

    /**
     * 回车换行符
     */
    private static final String CARRIAGE_RETURN = "\r\n";

    private static final String KEY_VALUE_SEPARATOR = ":";

    /**
     * 响应的基础信息
     */
    public static final String BASIC_RESPONSE = "HTTP/1.1 {statusCode} OK" + CARRIAGE_RETURN +
            "Vary: Accept-Encoding";

    public void handleSelectKey() {
        READ_BUFFER.clear();
        socketChannel = (SocketChannel) selectionKey.channel();
        try {
            while (socketChannel.read(READ_BUFFER) > 0) {
            }

            READ_BUFFER.flip();

            byte[] bytes = new byte[READ_BUFFER.limit()];
            READ_BUFFER.get(bytes);

            String requestMessage = new String(bytes,MarsConstant.ENCODING);
            log.info(requestMessage);
            // TODO
            requestURI = new RequestURI(getUri(requestMessage));
            requestMethod = requestMessage.substring(0, requestMessage.indexOf(" "));
            parseHeader(requestMessage);

            if(!requestMethod.toUpperCase().equals("GET")){
                requestBody = new ByteArrayInputStream(bytes);
            }

            MarsServerHandler marsServerHandler = new MarsServerHandler();
            marsServerHandler.request(this);

            staticHandler();

        } catch (IOException e) {
            log.error("处理请求异常异常", e);
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
     * 获取请求的资源地址
     *
     * @param request
     * @return
     */
    private static String getUri(String request) {
        //GET /index.html HTTP/1.1
        int firstBlank = request.indexOf(" ");
        String excludeMethod = request.substring(firstBlank + 1);
        return excludeMethod.substring(0, excludeMethod.indexOf(" "));
    }

    public void sendText(int statusCode, String text){
        this.statusCode = statusCode;
        this.sendText = text;
    }

    private void parseHeader(String headerStr) {
        if (StringUtil.isNull(headerStr)) {
            return;
        }

        // 解析请求头第一行
        int index = headerStr.indexOf(CARRIAGE_RETURN);
        if (index == -1) {
            return;
        }

        String firstLine = headerStr.substring(0, index);
        String[] parts = firstLine.split(" ");

        /*
         * 请求头的第一行必须由三部分构成，分别为 METHOD PATH VERSION
         * 比如：
         *     GET /index.html HTTP/1.1
         */
        if (parts.length < 3) {
            return;
        }

//        headers.setMethod(parts[0]);
//        headers.setPath(parts[1]);
//        headers.setVersion(parts[2]);

        // 解析请求头属于部分
        parts = headerStr.split(CARRIAGE_RETURN);
        for (String part : parts) {
            index = part.indexOf(KEY_VALUE_SEPARATOR);
            if (index == -1) {
                continue;
            }
            String key = part.substring(0, index);
            if (index == -1 || index + 1 >= part.length()) {
                continue;
            }
            String value = part.substring(index + 1);
            setRequestHeader(key, value);
        }
    }

    /**
     * 静态资源处理器
     *
     * @return
     */
    private void staticHandler() throws IOException {
        if(StringUtil.isNull(sendText)){
            return;
        }

        byte[] bytes = sendText.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);

        /* 加载初始化头 */
        buffer.put(BASIC_RESPONSE.replace("{statusCode}", String.valueOf(statusCode)).getBytes(MarsConstant.ENCODING));
        buffer.put(CARRIAGE_RETURN.getBytes(MarsConstant.ENCODING));
        buffer.put(("content-length: " + bytes.length).getBytes(MarsConstant.ENCODING));
        buffer.put(CARRIAGE_RETURN.getBytes(MarsConstant.ENCODING));

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

            buffer.put((entry.getKey() + ":" + valueStr.toString()).getBytes(MarsConstant.ENCODING));
            buffer.put(CARRIAGE_RETURN.getBytes(MarsConstant.ENCODING));
        }

        /* 加载要响应的数据 */
        buffer.put(bytes);
        buffer.flip();

        /* 开始响应 */
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
