package com.mars.mvc.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.common.annotation.api.MarsDataCheck;
import com.mars.common.util.MesUtil;
import com.mars.common.util.StringUtil;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验前端传参
 */
public class ParamsCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(ParamsCheckUtil.class);

    /**
     * 校验参数
     * @param params 参数集合
     * @return 校验结果
     */
    public static JSONObject checkParam(Object[] params){
        if(params == null){
            return null;
        }
        Class requestClass = HttpMarsRequest.class;
        Class responseClass = HttpMarsResponse.class;
        Class mapClass = Map.class;

        for(Object obj : params){
            if(obj == null){
                return null;
            }
            Class cls = obj.getClass();
            if(requestClass.equals(cls) || responseClass.equals(cls) || mapClass.equals(cls)){
                continue;
            }
            JSONObject result = checkParam(cls,obj);
            if(result != null){
                return result;
            }
        }
        return null;
    }

    /**
     * 校验参数
     * @param cls 参数类型
     * @param obj 参数对象
     * @return 校验结果
     */
    private static JSONObject checkParam(Class<?> cls, Object obj) {
        try {
            Field[] fields = cls.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                MarsDataCheck marsDataCheck = field.getAnnotation(MarsDataCheck.class);
                if(marsDataCheck == null){
                    continue;
                }
                Object val = field.get(obj);

                int errorCode = 1128;

                if(!reg(val,marsDataCheck.reg())){
                    return MesUtil.getMes(errorCode,marsDataCheck.msg());
                }

                if(!notNull(marsDataCheck, val)){
                    return MesUtil.getMes(errorCode,marsDataCheck.msg());
                }
            }
            return null;
        } catch (Exception e){
            logger.error("校验参数出现异常",e);
            return null;
        }
    }

    /**
     * 校验正则
     * @param val 数据
     * @param reg 正则
     * @return 结果
     */
    private static boolean reg(Object val,String reg){
        if(StringUtil.isNull(reg)){
            return true;
        }
        if(StringUtil.isNull(val)){
            return false;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(val.toString());
        return matcher.matches();
    }

    /**
     * 校验长度
     * @param val 数据
     * @param marsDataCheck 注解
     * @return 结果
     */
    private static boolean length(MarsDataCheck marsDataCheck, Object val){
        long valLen = val.toString().length();
        if(valLen < marsDataCheck.minLength() || valLen > marsDataCheck.maxLength()){
            return false;
        }
        return true;
    }

    /**
     * 非空校验
     * @param marsDataCheck 注解
     * @param val 数据
     * @return 结果
     */
    private static boolean notNull(MarsDataCheck marsDataCheck, Object val){
        if(!marsDataCheck.notNull()){
            return true;
        }
        if(StringUtil.isNull(val)){
            return false;
        }
        return length(marsDataCheck, val);
    }
}
