package com.mars.exp.core.aop;

import com.mars.aop.base.BaseAop;

/**
 * AOP示例
 */
public class ExpAop implements BaseAop {

    /**
     * 被监听的方法执行前执行这个方法
     * @param args 被监听的方法的参数
     */
    @Override
    public void startMethod(Object[] args) {
        System.out.println("开始执行AOP");
    }

    /**
     * 被监听的方法执行结束后执行这个方法
     * @param args 被监听的方法的参数
     * @param result 被监听方法的返回值
     */
    @Override
    public void endMethod(Object[] args, Object result) {
        System.out.println("执行AOP结束");
    }

    /**
     * 被监听的方法出异常了，执行这个方法
     * @param e 异常
     */
    @Override
    public void exp(Throwable e) {
        System.out.println("方法出异常了");
    }
}
