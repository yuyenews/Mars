package com.mars.mj.helper.templete;

import com.mars.mj.helper.model.PageModel;
import com.mars.mj.helper.model.PageParamModel;

import java.util.List;
import java.util.Map;

/**
 * 分页
 */
public class JdbcPage {


    /**
     * 有参查询列表
     * @param sql sql语句
     * @param param 参数
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static PageModel<Map> selectList(String sql, PageParamModel param, String dataSourceName) throws Exception {
        return selectList(sql,param, Map.class,dataSourceName);
    }

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @param dataSourceName 连接名
     * @return 数据
     */
    public static <T> PageModel<T> selectList(String sql, PageParamModel param, Class<T> cls, String dataSourceName) throws Exception {

        PageModel<T> pageModel = new PageModel<>();

        String selectSql = getSelectSql(sql);
        String countSql = getCountSql(sql);

        Map<String,Object> pageParam = getParam(param);
        List<T> dataList = BaseSelect.selectList(selectSql, pageParam, cls, dataSourceName);
        List<Map> countList = BaseSelect.selectList(countSql, pageParam, Map.class, dataSourceName);

        if (countList != null && countList.size() == 1) {
            Map countItem = countList.get(0);
            if (countItem != null && countItem.size() > 0) {
                Object countNum = countItem.get("countNum");
                if (countNum == null || countNum.toString().equals("")) {
                    countNum = 0;
                }
                pageModel.setPageCount(Integer.parseInt(countNum.toString()));
            }
        }

        pageModel.setDataList(dataList);
        pageModel.setCurrentPage(param.getCurrentPage());
        pageModel.setPageSize(param.getPageSize());

        int pageTotal = pageModel.getPageCount() / pageModel.getPageSize();

        if (pageModel.getPageCount() % pageModel.getPageSize() == 0) {
            pageModel.setPageTotal(pageTotal);
        } else {
            pageModel.setPageTotal(pageTotal + 1);
        }
        return pageModel;
    }

    /**
     * 获取总数sql
     * @param sql
     * @return
     */
    private static String getCountSql(String sql){
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append("select count(0) countNum from (");
        sqlBuilder.append(sql);
        sqlBuilder.append(") t");

        return sqlBuilder.toString();
    }

    /**
     * 获取分页sql
     * @param sql
     * @return
     */
    private static String getSelectSql(String sql){
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit #{pageStart},#{pageSize}");
        return sqlBuilder.toString();
    }

    /**
     * 重组参数
     * @param param
     * @return
     */
    private static Map<String,Object> getParam(PageParamModel param){
        Map<String,Object> objectMap = param.getParam();
        objectMap.put("pageStart",(param.getCurrentPage()-1) * param.getPageSize());
        objectMap.put("pageSize",param.getPageSize());
        return objectMap;
    }
}
