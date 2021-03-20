package com.mars.common.base.config.model;

import java.util.Calendar;

/**
 * JWT配置
 */
public class JWTConfig {

    /**
     * token秘钥
     */
    private String secret = "b18af1cf-563a-4394-ac98-0b31013c7ba5";
    /**
     * token 过期时间单位：秒
     */
    private int calendarField = Calendar.SECOND;

    /**
     * token 过期时间: 1天
     */
    private int calendarInterval = 86400;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getCalendarField() {
        return calendarField;
    }

    public void setCalendarField(int calendarField) {
        this.calendarField = calendarField;
    }

    public int getCalendarInterval() {
        return calendarInterval;
    }

    public void setCalendarInterval(int calendarInterval) {
        this.calendarInterval = calendarInterval;
    }
}
