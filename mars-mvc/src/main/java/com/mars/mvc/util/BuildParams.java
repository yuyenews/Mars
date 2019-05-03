package com.mars.mvc.util;

import com.mars.core.constant.MarsCloudConstant;
import com.mars.core.enums.DataType;
import com.mars.core.util.SerializableUtil;
import com.mars.server.server.request.HttpRequest;
import com.mars.server.server.request.HttpResponse;
import com.mars.server.server.request.model.FileUpLoad;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    public static Object[] builder(Method method, HttpRequest request, HttpResponse response) throws Exception {
        try {
            String requestName = HttpRequest.class.getSimpleName().toLowerCase();
            String responseName = HttpResponse.class.getSimpleName().toLowerCase();
            String mapName = Map.class.getSimpleName().toLowerCase();
            Class[] paramTypes = method.getParameterTypes();
            if(paramTypes == null || paramTypes.length < 1){
                return null;
            }
            Object[] params = new Object[paramTypes.length];
            for(int i = 0;i<paramTypes.length;i++){
                Class cls = paramTypes[i];
                String paramName = cls.getSimpleName().toLowerCase();
                if(requestName.equals(paramName)){
                    params[i] = request;
                } else if(responseName.equals(paramName)){
                    params[i] = response;
                } else if(mapName.equals(paramName)) {
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
    private static Object getObject(Class cls,HttpRequest request) throws Exception {
        /* 如果参数类型既不是request，也不是response，那么就当做一个对象来处理 */
        Object obj = cls.getDeclaredConstructor().newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields){
            Object val = request.getParemeter(f.getName());
            FileUpLoad fileUpLoad = request.getFile(f.getName());
            if(fileUpLoad != null){
                f.setAccessible(true);
                f.set(obj,fileUpLoad);
            } else if(val != null){
                f.setAccessible(true);
                String fieldTypeName = f.getType().getSimpleName().toUpperCase();
                String valStr = val.toString();
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
                    default:
                        f.set(obj,val);
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
    private static Object builderCloudObject(Class cls,HttpRequest request) throws Exception {
        Object requestType = request.getParemeter(MarsCloudConstant.REQUEST_TYPE);
        if(requestType != null && requestType.toString().equals(MarsCloudConstant.REQUEST_TYPE)){
            FileUpLoad fileUpLoad = request.getFile(MarsCloudConstant.PARAM);
            byte[] bytes = fileUpLoad.getBytes();
            return SerializableUtil.deSerialization(bytes, cls);
        }
        return null;
    }
}
