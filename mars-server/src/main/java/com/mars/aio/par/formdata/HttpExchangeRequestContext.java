/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mars.aio.par.formdata;

import com.mars.common.constant.MarsConstant;
import com.mars.aio.server.impl.MarsHttpExchange;
import org.apache.commons.fileupload.UploadContext;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

/**
 * 这个类拷贝自apache的common-fileupload项目
 * 做了少量的修改，将servletRequest换成了MarsHttpExchange，同时修改了多个方法的实现
 * 类名也做了更改
 *
 * <p>Provides access to the request information needed for a request made to
 * an HTTP servlet.</p>
 *
 * @since FileUpload 1.1
 */
public class HttpExchangeRequestContext implements UploadContext {

    // ----------------------------------------------------- Instance Variables

    /**
     * The request for which the context is being provided.
     */
    private final MarsHttpExchange request;

    /**
     * 请求类型
     */
    private String contentType;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a context for this request.
     *
     * @param request The request to which this context applies.
     * @param contentType 请求类型
     */
    public HttpExchangeRequestContext(MarsHttpExchange request, String contentType) {
        this.request = request;
        this.contentType = contentType;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Retrieve the character encoding for the request.
     *
     * @return The character encoding for the request.
     */
    public String getCharacterEncoding() {
        return MarsConstant.ENCODING;
    }

    /**
     * Retrieve the content type of the request.
     *
     * @return The content type of the request.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Retrieve the content length of the request.
     *
     * @return The content length of the request.
     * @deprecated 1.3 Use {@link #contentLength()} instead
     */
    @Deprecated
    public int getContentLength() {
        int size = 0;
        try {
            long length = contentLength();
            size = (int)length;
            if(size < 0){
                throw new Exception();
            }
        } catch (Exception e){
            try {
                size = request.getRequestBody().available();
            } catch (Exception e2){
            }
        }
        return size;
    }

    /**
     * Retrieve the content length of the request.
     *
     * @return The content length of the request.
     * @since 1.3
     */
    public long contentLength() {
        long size = 0;
        try {
            size = request.getRequestContentLength();
            if(size < 0){
                throw new Exception();
            }
        } catch (Exception e) {
            try {
                size = request.getRequestBody().available();
            } catch (Exception e2){
            }
        }
        return size;
    }

    /**
     * Retrieve the input stream for the request.
     *
     * @return The input stream for the request.
     *
     * @throws IOException if a problem occurs.
     */
    public InputStream getInputStream() throws IOException {
        return request.getRequestBody();
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return format("ContentLength=%s, ContentType=%s",
                Long.valueOf(this.contentLength()),
                this.getContentType());
    }

}
