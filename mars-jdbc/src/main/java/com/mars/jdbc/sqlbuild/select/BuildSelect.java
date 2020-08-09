package com.mars.jdbc.sqlbuild.select;


import com.alibaba.fastjson.annotation.JSONField;
import com.mars.common.util.StringUtil;
import com.mars.jdbc.sqlbuild.enums.JoinType;
import com.mars.jdbc.sqlbuild.enums.OrderByType;

import java.lang.reflect.Field;

public class BuildSelect {

    private String select;
    private String formTable;
    private StringBuffer join;
    private String where;
    private String orderBy;
    private String groupBy;

    private BuildSelect(){
        select = "";
        formTable = "";
        join = new StringBuffer("");
        where = "";
        orderBy = "";
        groupBy = "";
    }

    public static BuildSelect get(){
        return new BuildSelect();
    }

    public BuildSelect select(Class entity) {
        Field[] fields = entity.getDeclaredFields();
        if (fields == null || fields.length < 1) {
            return this;
        }

        StringBuffer queryField = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (i > 0) {
                queryField.append(",");
            }
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if (jsonField != null && !StringUtil.isNull(jsonField.name())) {
                queryField.append(jsonField.name());
            } else {
                queryField.append(field.getName());
            }
        }

        this.select = queryField.toString();
        return this;
    }

    public BuildSelect form(String tableName){
        StringBuffer sql = new StringBuffer();
        sql.append(" form ");
        sql.append(tableName);
        this.formTable = sql.toString();
        return this;
    }

    public BuildSelect leftJoin(String tableName, String fieldLeft, String fieldRight){
        return join(tableName,fieldLeft,fieldRight,JoinType.LEFT);
    }

    public BuildSelect rightJoin(String tableName, String fieldLeft, String fieldRight){
       return join(tableName,fieldLeft,fieldRight,JoinType.RIGHT);
    }

    public BuildSelect join(String tableName, String fieldLeft, String fieldRight, JoinType joinType){
        StringBuffer sql = new StringBuffer();
        sql.append(" ");
        sql.append(joinType.getCode());
        sql.append(" ");
        sql.append(tableName);
        sql.append(" on(");
        sql.append(fieldLeft);
        sql.append("=");
        sql.append(fieldRight);
        sql.append(") ");
        this.join.append(sql.toString());
        return this;
    }

    public BuildSelect where(BuildWhere buildWhere){
        this.where = buildWhere.build();
        return this;
    }

    public BuildSelect orderByAsc(String fieldName){
        return orderBy(fieldName,OrderByType.ASC);
    }

    public BuildSelect orderByDesc(String fieldName){
       return orderBy(fieldName,OrderByType.DESC);
    }

    public BuildSelect orderBy(String fieldName, OrderByType orderByType){
        StringBuffer sql = new StringBuffer();
        sql.append(" order by ");
        sql.append(fieldName);
        sql.append(" ");
        sql.append(orderByType.getCode());
        sql.append(" ");
        this.orderBy = sql.toString();
        return this;
    }

    public BuildSelect groupBy(String fieldName){
        StringBuffer sql = new StringBuffer();
        sql.append(" group by ");
        sql.append(fieldName);
        sql.append(" ");

        this.groupBy = sql.toString();
        return this;
    }

    public String build(){
        StringBuffer sql = new StringBuffer("select ");
        sql.append(select);
        sql.append(formTable);
        sql.append(join);
        sql.append(where);
        sql.append(orderBy);
        sql.append(groupBy);
        return sql.toString();
    }

    public static void main(String[] args) {
        String sql = BuildSelect.get()
                .select(Test.class)
                .form("user_info")
                .leftJoin("user_info_b","user.id","userb.user_id")
                .leftJoin("score_info","sc.user_id","user.id")
                .rightJoin("class_info","cls.id","user.cls_id")
                .where(new BuildWhere())
                .orderByAsc("cls.id")
                .groupBy("user.id")
                .build();

        System.out.println(sql);
    }
}

class Test{
    private String name;

    @JSONField(name = "user.ageNum")
    private String age;

    private int height;
}