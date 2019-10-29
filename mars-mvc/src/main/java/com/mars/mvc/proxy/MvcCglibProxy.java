package com.mars.mvc.proxy;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.annotation.MarsReference;
import com.mars.core.annotation.enums.RefType;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsBeanModel;
import com.mars.core.util.StringUtil;
import com.mars.mvc.logs.LogAop;
import com.mars.core.annotation.MarsLog;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理类
 * @author yuye
 *
 */
public class MvcCglibProxy implements MethodInterceptor {

	private Enhancer enhancer;

	private Class cls;

	/**
	 * 所有的bean对象
	 */
	private Map<String, MarsBeanModel> beanModelMap = LoadHelper.getBeanObjectMap();
	
	/**
	 * 获取代理对象
	 * @param clazz  bean的class
	 * @return 对象
	 */
	public Object getProxy(Class<?> clazz) {
	    this.cls = clazz;

		enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}
	
	
	/**
	 * 绑定代理
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		LogAop c = null;

		MarsLog marsLog = method.getAnnotation(MarsLog.class);
		if(marsLog != null){
			c = new LogAop(cls,method.getName());
			c.startMethod(args);
		}

		try{
			Object o1 = executeRef(method,args);

			if(c != null){
				c.endMethod(args,o1);
			}

			return getSuccessResult(o1);
		} catch (Throwable e) {
			if(c != null) {
				c.exp(e);
			}
			return getErrorResult(e);
		}
	}

	/**
	 * 指定服务层的方法
	 * @param method 方法
	 * @param args 参数
	 * @return 返回值
	 * @throws Exception 异常
	 */
	private Object executeRef(Method method,Object[] args) throws Exception {
		/* 根据注解获取到对应的bean对象实体 */
		MarsReference marsReference = method.getAnnotation(MarsReference.class);
		MarsBeanModel marsBeanModel = getMarsBeanModel(marsReference);

		/* 获取bean对象的class和实例 */
		Class<?> cls = marsBeanModel.getCls();
		Object obj = marsBeanModel.getObj();

		if(marsReference.refType().equals(RefType.METHOD)){
			/* 如果引用的是一个方法则执行bean里面对应的方法 */
			Object result = executeRefMethod(cls,obj,args,marsReference);
			if(!result.equals("errorRef")){
				return result;
			}
		} else {
			/* 否则就将bean里面对应的属性的值返回 */
			Field field = cls.getDeclaredField(marsReference.refName());
			if(field == null){
				throw new Exception("没有找到名称为["+marsReference.refName()+"]的属性");
			}
			field.setAccessible(true);
			return field.get(obj);
		}

		return getFailResult(method.getName()+"方法引用的资源不正确");
	}

	/**
	 * 获取要执行的bean对象
	 * @param marsReference 引用注解
	 * @return bean对象实体类
	 * @throws Exception 异常
	 */
	private MarsBeanModel getMarsBeanModel(MarsReference marsReference) throws Exception {
		if(marsReference == null || StringUtil.isNull(marsReference.beanName())
			|| StringUtil.isNull(marsReference.refName())){
			throw new Exception("没有配置MarsReference注解或者配置不正确");
		}

		MarsBeanModel marsBeanModel = beanModelMap.get(marsReference.beanName());
		if(marsBeanModel == null){
			throw new Exception("没有找到name为["+marsReference.beanName()+"]的MarsBean");
		}
		return marsBeanModel;
	}

	/**
	 * 执行方法
	 * @param cls 类
	 * @param obj 对象
	 * @param args 参数
	 * @param marsReference 引用注解
	 * @return 返回值
	 * @throws Exception 异常
	 */
	private Object executeRefMethod(Class<?> cls,Object obj,Object[] args, MarsReference marsReference) throws Exception {
		Method[] methods = cls.getDeclaredMethods();
		for(Method methodItem : methods){
			if(methodItem.getName().equals(marsReference.refName())){
				return methodItem.invoke(obj,args);
			}
		}
		return "errorRef";
	}

	/**
	 * 成功的返回数据结构
	 * @param obj bean的返回值
	 * @return json
	 */
	private JSONObject getSuccessResult(Object obj){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code","success");
		jsonObject.put("result",obj);
		return jsonObject;
	}

	/**
	 * 失败的返回数据结构
	 * @param obj 错误提示
	 * @return json
	 */
	private JSONObject getFailResult(Object obj){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code","fail");
		jsonObject.put("msg",obj);
		return jsonObject;
	}

	/**
	 * 异常的返回数据结构
	 * @param e 错误提示
	 * @return json
	 */
	private JSONObject getErrorResult(Throwable e){
		if(e instanceof InvocationTargetException){
			InvocationTargetException targetException = (InvocationTargetException)e;
			return getFailResult(targetException.getTargetException().getMessage());
		}
		return getFailResult(e.getMessage());
	}
}
