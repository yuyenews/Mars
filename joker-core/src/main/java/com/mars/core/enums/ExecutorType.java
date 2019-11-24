package com.mars.core.enums;

public enum ExecutorType {

    SIMPLE("SIMPLE"),
    REUSE("REUSE"),
    BATCH("BATCH");

    private String code;

    ExecutorType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
