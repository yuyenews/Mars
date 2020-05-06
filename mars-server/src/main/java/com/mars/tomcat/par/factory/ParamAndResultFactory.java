package com.mars.tomcat.par.factory;

import com.mars.tomcat.par.base.BaseParamAndResult;

public class ParamAndResultFactory {

    private static BaseParamAndResult baseParamAndResult;

    public static void setBaseParamAndResult(BaseParamAndResult baseParamAndResult) {
        ParamAndResultFactory.baseParamAndResult = baseParamAndResult;
    }

    public static BaseParamAndResult getBaseParamAndResult(){
        return ParamAndResultFactory.baseParamAndResult;
    }
}
