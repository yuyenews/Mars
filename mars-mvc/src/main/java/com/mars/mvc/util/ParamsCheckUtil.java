package com.mars.mvc.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.annotation.MarsDataCheck;
import com.mars.core.util.MesUtil;
import com.mars.core.util.StringUtil;
import com.mars.server.server.request.HttpMarsRequest;
import com.mars.server.server.request.HttpMarsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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

                if(!notNull(marsDataCheck, val,marsDataCheck.length())){
                    return MesUtil.getMes(errorCode,marsDataCheck.msg());
                }

                if(!number(marsDataCheck, val,marsDataCheck.length())){
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
     * @param length 长度
     * @return 结果
     */
    private static boolean length(Object val,Long length){
        if(val.toString().length() > length){
            return false;
        }
        return true;
    }

    /**
     * 非空校验
     * @param marsDataCheck 注解
     * @param val 数据
     * @param length 长度
     * @return 结果
     */
    private static boolean notNull(MarsDataCheck marsDataCheck, Object val,Long length){
        if(!marsDataCheck.notNull()){
            return true;
        }
        if(StringUtil.isNull(val)){
            return false;
        }
        return length(val,length);
    }

    /**
     * 数字校验
     * @param marsDataCheck 集合
     * @param val 数据
     * @param length 长度
     * @return 结果
     */
    private static boolean number(MarsDataCheck marsDataCheck, Object val,Long length){
        if(!marsDataCheck.number()){
            return true;
        }
        boolean result = reg(val,"(^[\\-0-9][0-9]*(.[0-9]+)?)$");
        if(result){
            BigDecimal fromVal = new BigDecimal(val.toString());
            BigDecimal len = new BigDecimal(length.toString());
            if(fromVal.compareTo(len) > 0){
                return false;
            }
            return true;
        }
        return false;
    }
}
