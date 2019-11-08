package com.mars.jdbc.helper.model;

import java.util.HashMap;
import java.util.Map;

public class PageParamModel {

    private Map<String,Object> param;

    private int currentPage;

    private int pageSize;

    public Map<String, Object> getParam() {
        if(param == null){
            return new HashMap<>();
        }
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
