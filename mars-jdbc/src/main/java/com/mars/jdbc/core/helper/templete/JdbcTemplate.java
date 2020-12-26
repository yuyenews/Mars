package com.mars.jdbc.core.helper.templete;

import com.mars.jdbc.core.helper.templete.model.PageModel;
import com.mars.jdbc.core.helper.templete.model.PageParamModel;
import com.mars.jdbc.core.helper.templete.base.BaseJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * jdbc模板
 */
public class JdbcTemplate extends BaseJdbcTemplate {

    private String dataSourceName;

    private JdbcTemplate() {
    }

    /**
     * 获取JdbcTemplete对象
     *
     * @return
     */
    public static JdbcTemplate get() {
        return get(null);
    }

    /**
     * 获取JdbcTemplete对象
     *
     * @param dataSourceName
     * @return
     */
    public static JdbcTemplate get(String dataSourceName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.dataSourceName = jdbcTemplate.getDataSourceName(dataSourceName);
        return jdbcTemplate;
    }

    /**
     * 查询多条数据
     *
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map> selectList(String sql, Object param) throws Exception {
        return JdbcSelect.selectList(sql,param,dataSourceName);
    }

    /**
     * 查询多条数据
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public List<Map> selectList(String sql) throws Exception {
        return JdbcSelect.selectList(sql,dataSourceName);
    }

    /**
     * 无参查询列表，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @return 数据
     */
    public <T> List<T> selectList(String sql, Class<T> cls) throws Exception {
        return JdbcSelect.selectList(sql,cls,dataSourceName);
    }

    /**
     * 有参查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @return 数据
     */
    public <T> List<T> selectList(String sql, Object param, Class<T> cls) throws Exception {
        return JdbcSelect.selectList(sql,param,cls,dataSourceName);
    }

    /**
     * 查询一条数据
     *
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectOne(String sql, Object param) throws Exception {
        return JdbcSelect.selectOne(sql,param,dataSourceName);
    }

    /**
     * 查询一条数据
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectOne(String sql) throws Exception {
        return JdbcSelect.selectOne(sql,dataSourceName);
    }


    /**
     * 无参查询一条，指定返回类型
     * @param sql sql语句
     * @param cls 返回类型
     * @return 数据
     */
    public <T> T selectOne(String sql, Class<T> cls) throws Exception {
        return JdbcSelect.selectOne(sql,cls,dataSourceName);
    }

    /**
     * 有参查询一条，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @return 数据
     */
    public <T> T selectOne(String sql, Object param, Class<T> cls) throws Exception {
        return JdbcSelect.selectOne(sql,param,cls,dataSourceName);
    }

    /**
     * 有参分页查询列表
     * @param sql sql语句
     * @param param 参数
     * @return 数据
     */
    public PageModel<Map> selectPageList(String sql, PageParamModel param) throws Exception {
        return JdbcPage.selectList(sql, param,dataSourceName);
    }

    /**
     * 有参分页查询列表，指定返回类型
     * @param sql sql语句
     * @param param 参数
     * @param cls 返回类型
     * @return 数据
     */
    public <T> PageModel<T> selectPageList(String sql, PageParamModel param, Class<T> cls) throws Exception {
        return JdbcPage.selectList(sql,param,cls,dataSourceName);
    }

    /**
     * 增删改
     *
     * @param sql
     * @param param
     * @return 结果
     * @throws Exception 异常
     */
    public int update(String sql, Object param) throws Exception {
        return JdbcUpdate.update(sql,param,dataSourceName);
    }

    /**
     * 增删改
     *
     * @param sql
     * @return 结果
     * @throws Exception 异常
     */
    public int update(String sql) throws Exception {
       return JdbcUpdate.update(sql,dataSourceName);
    }

    /**
     * 获取最后一次插入的ID
     * @return
     * @throws Exception
     */
    public Object getLastInsertID() throws Exception {
        Map<String,Object> result =  selectOne("select LAST_INSERT_ID()");
        if(result == null || result.size() < 1){
            return null;
        }
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }
}
