package com.yuyenews.core.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSlf4j extends GogeLogger {

    private Logger logger;

    public LoggerSlf4j(Class cls){
        logger = LoggerFactory.getLogger(cls);
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
