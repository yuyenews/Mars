package com.mars.exp.api.inters;

import com.mars.common.annotation.api.MarsInterceptor;
import com.mars.mvc.base.BaseInterceptor;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截所有exp开头的api
 */
@MarsInterceptor(pattern = "/exp*")
public class ExpInter implements BaseInterceptor {

    /**
     * api执行前
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeRequest(HttpMarsRequest request, HttpMarsResponse response) {
        System.out.println("开始执行拦截器");
        // 返回SUCCESS表示通过，如果不通过的话，直接返回对象或者提示信息，前端会收到
        return SUCCESS;
    }

    /**
     * api执行后
     * @param request
     * @param response
     * @param obj 控制层返回的数据
     * @return
     */
    @Override
    public Object afterRequest(HttpMarsRequest request, HttpMarsResponse response, Object obj) {
        System.out.println("执行拦截器结束");
        // 返回SUCCESS表示通过，如果不通过的话，直接返回对象或者提示信息，前端会收到
        return SUCCESS;
    }

    /**
     * 有些被拦截规则拦截了的api，但是这个拦截器又不打算处理，
     * 可以添加到List中，并在这个方法里返回
     * @return
     */
    @Override
    public List<String> exclude() {
        // 这两个api直接通过，不拦截
        List<String> list = new ArrayList<>();
        list.add("/expPostRequest");
        list.add("/expUploadRequest");
        return list;
    }
}
