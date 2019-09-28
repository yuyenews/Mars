package com.mars.jdbc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.MarsConstant;
import com.mars.core.util.ConfigUtil;

/**
 * JDBC配置
 */
public class JdbcConfigUtil {

    /**
     * 获取JDBC配置信息
     *
     * @return 配置信息
     */
    public static JSONObject getJdbcConfig() throws Exception {
        try {
            JSONObject jsonObject = ConfigUtil.getConfig();

            if (jsonObject != null) {

                JSONObject jdbc = jsonObject.getJSONObject("jdbc");

                return jdbc;
            }
        } catch (Exception e) {
            throw new Exception("从配置文件中读取jdbc模块配置出错",e);
        }
        return new JSONObject();
    }

    /**
     * 获取JDBC配置信息
     *
     * @return 配置信息
     */
    public static Object getJdbcConfig(String key) throws Exception {
        try {
            JSONObject jdbcConfig = getJdbcConfig();
            return jdbcConfig.get(key);
        } catch (Exception e) {
            throw new Exception("从配置文件中读取jdbc模块配置出错",e);
        }
    }

    /**
     * 读取数据源
     * @return
     * @throws Exception
     */
    public static JSONArray getJdbcDataSourceList() throws Exception{
        try {
            JSONArray dataSourceList = new JSONArray();
            Object dataSources = getJdbcConfig(MarsConstant.DATA_SOURCE);
            if(dataSources instanceof JSONArray){
                dataSourceList = (JSONArray)dataSources;
            } else {
                dataSourceList.add(dataSources);
            }
            return dataSourceList;
        } catch (Exception e){
            throw new Exception("从配置文件中读取jdbc模块的数据源配置出错",e);
        }
    }
}
