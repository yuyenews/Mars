package com.mars.mvc.util;

import com.mars.core.constant.MarsCloudConstant;
import com.mars.core.enums.DataType;
import com.mars.core.util.SerializableUtil;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import com.mars.server.server.request.model.MarsFileUpLoad;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 构建参数
 */
public class BuildParams {

    /**
     * 构建controller的传参
     * @param method
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static Object[] builder(Method method, HttpMarsRequest request, HttpMarsResponse response) throws Exception {
        try {
            Class requestClass = HttpMarsRequest.class;
            Class responseClass = HttpMarsResponse.class;
            Class mapClass = Map.class;
            Class[] paramTypes = method.getParameterTypes();
            if(paramTypes == null || paramTypes.length < 1){
                return null;
            }
            Object[] params = new Object[paramTypes.length];
            for(int i = 0;i<paramTypes.length;i++){
                Class cls = paramTypes[i];
                if(requestClass.equals(cls)){
                    params[i] = request;
                } else if(responseClass.equals(cls)){
                    params[i] = response;
                } else if(mapClass.equals(cls)) {
                    params[i] = request.getParemeters();
                } else {
                    /* 先判断此次请求是不是cloud发起的，如果是的话就将对象反序列化出来 */
                    Object obj = builderCloudObject(cls,request);
                    if(obj == null){
                        /* 如果此次请求不是cloud发起的，那么就走正常取参 */
                        params[i] = getObject(cls,request);
                    } else {
                        params[i] = obj;
                    }
                }
            }
            return params;
        } catch (Exception e){
            throw new Exception("参数注入异常",e);
        }
    }

    /**
     * 构建参数对象
     * @param cls
     * @param request
     * @return
     * @throws Exception
     */
    private static Object getObject(Class cls, HttpMarsRequest request) throws Exception {
        /* 如果参数类型既不是request，也不是response，那么就当做一个对象来处理 */
        Object obj = cls.getDeclaredConstructor().newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields){
            List<Object> valList = request.getParameterValues(f.getName());
            MarsFileUpLoad marsFileUpLoad = request.getFile(f.getName());
            if(marsFileUpLoad != null){
                f.setAccessible(true);
                f.set(obj, marsFileUpLoad);
            } else if(valList != null && !valList.isEmpty()){
                f.setAccessible(true);
                String fieldTypeName = f.getType().getSimpleName().toUpperCase();
                String valStr = valList.get(0).toString();
                switch (fieldTypeName){
                    case DataType.INT:
                    case DataType.INTEGER:
                        f.set(obj,Integer.parseInt(valStr));
                        break;
                    case DataType.BYTE:
                        f.set(obj,Byte.parseByte(valStr));
                        break;
                    case DataType.STRING:
                    case DataType.CHAR:
                    case DataType.CHARACTER:
                        f.set(obj,valStr);
                        break;
                    case DataType.DOUBLE:
                        f.set(obj,Double.parseDouble(valStr));
                        break;
                    case DataType.FLOAT:
                        f.set(obj,Float.parseFloat(valStr));
                        break;
                    case DataType.LONG:
                        f.set(obj,Long.parseLong(valStr));
                        break;
                    case DataType.SHORT:
                        f.set(obj,Short.valueOf(valStr));
                        break;
                    case DataType.BOOLEAN:
                        f.set(obj,Boolean.parseBoolean(valStr));
                        break;
                    case DataType.DATE:
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        f.set(obj,simpleDateFormat.parse(valStr));
                        break;
                    case DataType.LIST:
                        f.set(obj,valList);
                        break;
                }
            }
        }
        return obj;
    }

    /**
     * 获取cloud通过序列化传来的参数对象
     * @param cls
     * @param request
     * @return
     * @throws Exception
     */
    private static Object builderCloudObject(Class cls, HttpMarsRequest request) throws Exception {
        Object requestType = request.getParameter(MarsCloudConstant.REQUEST_TYPE);
        if(requestType != null && requestType.toString().equals(MarsCloudConstant.REQUEST_TYPE)){
            MarsFileUpLoad marsFileUpLoad = request.getFile(MarsCloudConstant.PARAM);
            byte[] bytes = marsFileUpLoad.getBytes();
            return SerializableUtil.deSerialization(bytes, cls);
        }
        return null;
    }
}
