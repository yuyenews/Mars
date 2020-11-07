package com.mars.timer.execute;

import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
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
                int fixedRate = marsTimerModel.getMarsTimer().loop();
                loopTimer(fixedRate,marsTimerModel);
            }
        } catch (Exception e){
            marsLogger.error("加载定时任务出错",e);
        }
    }

    /**
     * 定时轮询
     * @param fixedRate 轮询间隔
     * @param marsTimerModel 对象
     */
    private static void loopTimer(int fixedRate, MarsTimerModel marsTimerModel){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                executeTimer(marsTimerModel);
            }
        }, new Date(),fixedRate);
    }

    /**
     * 开始执行
     * @param marsTimerModel 对象
     */
    private static void executeTimer(MarsTimerModel marsTimerModel){
        try {
            Object hasServerStart = constants.getAttr(MarsConstant.HAS_SERVER_START);
            if(hasServerStart != null){
                Object beanObject = marsTimerModel.getObj();
                Method method = marsTimerModel.getMethod();
                method.invoke(beanObject);
            }
        } catch (Exception e){
            marsLogger.error("执行定时任务出错,方法名:{}.{}", marsTimerModel.getCls().getName(), marsTimerModel.getMethod().getName(), e);
        }
    }
}
