package com.mars.exp.core.service;

import com.alibaba.fastjson.JSON;
import com.mars.common.annotation.bean.MarsAop;
import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.bean.MarsWrite;
import com.mars.exp.api.ExpApi;
import com.mars.exp.api.vo.ExpVO;
import com.mars.exp.core.aop.ExpAop;
import com.mars.exp.core.dao.ExpDAO;
import com.mars.server.server.request.model.MarsFileUpLoad;

import java.util.ArrayList;
import java.util.List;

/**
 * service层
 */
@MarsBean("expApiService")
public class ExpApiService implements ExpApi {

    /**
     * 为了让大家可以快速的跑起来，所以本示例没有连接数据库
     * 所以自然也就不会调用dao的方法了，这里注入进来的，只是为了演示IOC的用法
     */
    @MarsWrite
    private ExpDAO expDAO;

    /**
     * 这个方法上加了aop监听
     * @param expVO
     * @return
     */
    @Override
    @MarsAop(className = ExpAop.class)
    public List<ExpVO> expGetRequest(ExpVO expVO) {
        // 打印expDAO，如果不为null就说明已经注入了
        System.out.println(expDAO);

        // 打印接收到的参数，看是否接收成功
        System.out.println(expVO.getName());
        System.out.println(JSON.toJSONString(expVO.getNames()));

        // 返回数据
        return getExpResultData();
    }

    @Override
    public List<ExpVO> expPostRequest(ExpVO expVO) {
        // 打印expDAO，如果不为null就说明已经注入了
        System.out.println(expDAO);

        // 打印接收到的参数，看是否接收成功
        System.out.println(expVO.getName());
        System.out.println(JSON.toJSONString(expVO.getNames()));

        // 返回数据
        return getExpResultData();
    }

    @Override
    public String expUploadRequest(ExpVO expVO) {
        // 打印expDAO，如果不为null就说明已经注入了
        System.out.println(expDAO);

        // 打印接收到的参数，看是否接收成功
        MarsFileUpLoad marsFileUpLoad = expVO.getMarsFileUpLoad();
        if(marsFileUpLoad == null){
            return "上传失败";
        }
        System.out.println(marsFileUpLoad.getFileName());
        System.out.println(marsFileUpLoad.getInputStream());

        return "上传成功";
    }

    /**
     * 这是把返回数据写死了，不然你们还得搭环境，连接数据库
     * @return
     */
    private List<ExpVO> getExpResultData(){
        List<ExpVO> list = new ArrayList<>();

        ExpVO exp = new ExpVO();
        exp.setName("hello world");
        list.add(exp);

        exp = new ExpVO();
        exp.setName("The world dies");
        list.add(exp);

        return list;
    }
}
