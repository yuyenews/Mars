package com.mars.exp.core.dao;

import com.mars.common.annotation.jdbc.MarsDao;
import com.mars.exp.core.dto.ExpDTO;
import com.mars.jdbc.annotation.DataSource;
import com.mars.jdbc.annotation.MarsGet;
import com.mars.jdbc.annotation.MarsSelect;
import com.mars.jdbc.annotation.MarsUpdate;
import com.mars.jdbc.annotation.enums.OperType;
import com.mars.jdbc.helper.model.PageModel;
import com.mars.jdbc.helper.model.PageParamModel;
import com.mars.jdbc.helper.templete.JdbcTemplate;

import java.util.List;

@MarsDao
public abstract class ExpDAO {

    /**
     * 根据主键查询一条数据
     * @param id
     * @return
     */
    @MarsGet(tableName = "数据库表名",primaryKey = "id")
    public abstract ExpDTO selectById(int id);

    /**
     * 插入一条数据
     * @param expDTO
     * @return
     */
    @MarsUpdate(tableName = "数据库表名",operType = OperType.INSERT)
    public abstract int insert(ExpDTO expDTO);

    /**
     * 根据主键删除一条数据
     * @param id
     * @return
     */
    @MarsUpdate(tableName = "数据库表名",operType = OperType.DELETE,primaryKey = "id")
    public abstract int delete(int id);

    /**
     * 根据主键更新一条数据
     * @param expDTO
     * @return
     */
    @MarsUpdate(tableName = "数据库表名",operType = OperType.UPDATE,primaryKey = "id")
    public abstract int update(ExpDTO expDTO);

    /**
     * sql语句固定的查询
     * #{} 占位符，无sql注入风险
     * ${} 字符串拼接，有sql注入风险
     * @param expDTO
     * @return
     */
    @DataSource("数据源name，不传默认采用第一个")//如果要采用默认数据源的话，这个注解可以不配
    @MarsSelect(sql = "select a,b,c from 表名 where a=#{a} and b=${b}",resultType = ExpDTO.class)
    public abstract List<ExpDTO> selectList(ExpDTO expDTO);

    /**
     * sql语句固定的分页查询
     * @param pageParamModel
     * @return
     */
    @MarsSelect(sql = "select a,b,c from 表名 where a=#{a} and b=${b}",resultType = ExpDTO.class,page = true)
    public abstract PageModel<ExpDTO> selectPageList(PageParamModel pageParamModel);

    /**
     * sql语句不固定的查询
     * @param expDTO
     * @return
     * @throws Exception
     */
    public List<ExpDTO> selectList2(ExpDTO expDTO) throws Exception {
        StringBuffer sql = new StringBuffer();

        // 这里根据expDTO里的参数，判断，然后拼接sql就好了

        return JdbcTemplate.get("数据源name，不传默认采用第一个").selectList(sql.toString(),expDTO,ExpDTO.class);
    }

    /**
     * sql语句不固定的分页查询
     * @param expDTO
     * @return
     * @throws Exception
     */
    public PageModel<ExpDTO> selectPageList2(ExpDTO expDTO) throws Exception {
        StringBuffer sql = new StringBuffer();

        // 这里根据expDTO里的参数，判断，然后拼接sql就好了

        // 创建传参，实际开发中，这个对象需要从外面传进来，因为page和pageSize是活的，不能写死
        PageParamModel pageParamModel = PageParamModel.getPageParamModel(1,100);
        pageParamModel.setParam(expDTO);
        return JdbcTemplate.get().selectPageList(sql.toString(),pageParamModel,ExpDTO.class);
    }
}
