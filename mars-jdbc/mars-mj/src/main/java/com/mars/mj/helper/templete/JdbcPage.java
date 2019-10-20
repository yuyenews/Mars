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

        /* 将查询sql转化成分页所需的两条语句 */
        String selectSql = getSelectSql(sql);
        String countSql = getCountSql(sql);

        /* 将查询参数提取出来并转化成需要的格式 */
        Map<String,Object> pageParam = getParam(param);

        /* 查询总条数 */
        List<Map> countList = BaseSelect.selectList(countSql, pageParam, Map.class, dataSourceName);

        /* 获取总条数 */
        Object countNum = getCountNum(countList);
        if(countNum == null){
            return null;
        }

        /* 如果能进到这一步说明有数据，此刻再查询需要的数据 */
        List<T> dataList = BaseSelect.selectList(selectSql, pageParam, cls, dataSourceName);

        /* 组装返回对象 */
        PageModel<T> pageModel = new PageModel<>();
        pageModel.setPageCount(Integer.parseInt(countNum.toString()));
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
     * 从返回数据中获取总条数
     * @param countList 返回的数据
     * @return 总条数
     */
    private static Object getCountNum(List<Map> countList) {
        if (countList == null || countList.size() < 1) {
            return null;
        }

        Map countItem = countList.get(0);
        if (countItem == null || countItem.size() < 1) {
            return null;
        }

        Object countNum = countItem.get("countNum");
        if (countNum == null || countNum.toString().equals("")) {
            return null;
        }

        return countNum;
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
