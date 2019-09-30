package com.mars.mvc.remote.controller;

import com.mars.core.annotation.Controller;
import com.mars.core.annotation.MarsLog;
import com.mars.core.annotation.RequestMethod;
import com.mars.core.annotation.enums.RequestMetohd;
import com.mars.core.remote.config.RemoteConfigService;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改远程配置后 接受配置中心的通知
 */
@Controller
public class RemoteConfigController {

    /**
     * 重新加载配置
     * @param request
     * @param response
     * @return 结果
     */
    @RequestMethod(RequestMetohd.POST)
    @MarsLog
    public Map<String,Object> reloadConfig(HttpRequest request, HttpResponse response) {

        /* TODO(功能开发中) */

        Object config = request.getParameter("config");
        String result = RemoteConfigService.reloadConfig(config);

        Map<String,Object> returns = new HashMap<>();
        if(result.equals("ok")){
            returns.put("msg","通知成功");
            returns.put("success","ok");
        } else {
            returns.put("msg","通知失败");
            returns.put("success","no");
        }
        return returns;
    }
}
