package com.mars.timer.execute;

import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadHelper;
import com.mars.core.model.MarsTimerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 执行定时任务
 */
public class ExecuteMarsTimer {

    private static Logger marsLogger = LoggerFactory.getLogger(ExecuteMarsTimer.class);

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 执行
     */
    public static void execute() {
        try {
            List<MarsTimerModel> marsTimerModelList = LoadHelper.getMarsTimersList();
            for(MarsTimerModel marsTimerModel : marsTimerModelList){
                int loop = marsTimerModel.getMarsTimer().loop();
                /* 开启定时任务 */
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Object hasNettyStart = constants.getAttr(MarsConstant.HAS_NETTY_START);
                            if(hasNettyStart != null){
                                Object beanObject = marsTimerModel.getObj();
                                Method method = marsTimerModel.getMethod();
                                method.invoke(beanObject);
                            }
                        } catch (Exception e){
                            marsLogger.error("执行定时任务出错,方法名:"+marsTimerModel.getCls().getName()+"."+marsTimerModel.getMethod().getName(),e);
                        }
                    }
                }, new Date(),loop);
            }
        } catch (Exception e){
            marsLogger.error("加载定时任务出错",e);
        }
    }
}
