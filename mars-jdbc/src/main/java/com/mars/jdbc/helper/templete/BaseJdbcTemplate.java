package com.mars.jdbc.helper.templete;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.util.ThreadUtil;
import com.mars.jdbc.helper.base.DBHelper;
import com.mars.jdbc.helper.manager.ConnectionManager;
import com.mars.jdbc.helper.model.SqlBuilderModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc模板父类
 */
public class BaseJdbcTemplate {

    private static MarsSpace marsSpace = MarsSpace.getEasySpace();

    /**
     * 获取数据库连接
     *
     * @return
     * @throws Exception
     */
    protected static ConnectionManager getConnection(String dataSourceName) throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();

        /* 获取当前线程中的Connection */
        Object obj = ThreadUtil.getThreadLocal().get();

        /* 获取数据源名称 */
        String dataSourceName2 = getDataSourceName(dataSourceName);

        if (obj != null) {
            Map<String, Connection> connections = (Map<String, Connection>) obj;
            connectionManager.setConnection(connections.get(dataSourceName2));
            connectionManager.setHasTrantion(false);
        } else {
            connectionManager.setConnection(DBHelper.getConnection(dataSourceName2));
            connectionManager.setHasTrantion(true);
        }
        return connectionManager;
    }



    /**
     * 获取数据源名称
     *
     * @return str
     */
    protected static String getDataSourceName(String dataSourceName) {
        if (dataSourceName == null) {
            dataSourceName = marsSpace.getAttr(MarsConstant.DEFAULT_DATASOURCE_NAME).toString();
        }
        return dataSourceName;
    }

    /**
     * 构建sql语句
     *
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    protected static SqlBuilderModel builderSql(String sql, Object args) throws Exception {

        SqlBuilderModel sqlBuilderModel = new SqlBuilderModel();

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(args));

        List<Object> params = new ArrayList<>();

        sql = replaceMatcher(sql,params,jsonObject);
        sql = formatMatcher(sql,params,jsonObject);

        sqlBuilderModel.setSql(sql);
        sqlBuilderModel.setParams(params.toArray());

        return sqlBuilderModel;
    }

    /**
     * 替换占位符为问号
     * @param sql
     * @param params
     * @param jsonObject
     * @return
     */
    private static String formatMatcher(String sql,List<Object> params,JSONObject jsonObject){
        Pattern pattern = Pattern.compile("(#\\{((?!}).)*\\})");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String matcherName = matcher.group();
            sql = sql.replace(matcherName,"?");
            String filedName = getFiledName(matcherName,"#");
            params.add(jsonObject.get(filedName));
        }
        return sql;
    }

    /**
     * 替换占位符为具体的值
     * @param sql
     * @param params
     * @param jsonObject
     * @return
     */
    private static String replaceMatcher(String sql,List<Object> params,JSONObject jsonObject){
        Pattern pattern = Pattern.compile("(\\$\\{((?!}).)*\\})");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String matcherName = matcher.group();
            String filedName = getFiledName(matcherName,"$");
            sql = sql.replace(matcherName,jsonObject.getString(filedName));
        }
        return sql;
    }

    /**
     * 获取参数中的name
     * @param matcherStr
     * @param startStr
     * @return
     */
    private static String getFiledName(String matcherStr,String startStr){
        matcherStr = matcherStr.replace(startStr+"{","").replace("}","");
        return matcherStr;
    }
}

