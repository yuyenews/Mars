package com.yuyenews.core.logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerLog4j extends GogeLogger {

    private Logger logger;

    public LoggerLog4j(Class cls){
        logger = LogManager.getLogger(cls);
    }

    @Override
    public void info(String info) {
        logger.info(info);
    }

    @Override
    public void error(String info, Throwable e) {
        logger.error(info,e);
    }

    @Override
    public void warn(String info) {
        logger.warn(info);
    }

    @Override
    public void error(String info) {
        logger.error(info);
    }


}
