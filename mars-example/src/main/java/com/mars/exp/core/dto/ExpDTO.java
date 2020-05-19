package com.mars.exp.core.dto;

/**
 * 这里面尽量用包装器类型，比如用Integer不用int，
 * 因为包装器类型可以赋值为null，这样如果不需要被sql用到的时候，可以赋值为null，免被带入到sql
 */
public class ExpDTO {

    /**
     * 这里的字段名必须跟查询语句中的字段名 一模一样（如果取了别名就跟别名一样）
     * 如果是增删改操作，并且你用的是框架自带的方法，那么必须跟数据表的字段名一模一样
     * 如果数据库是user_name 那么这里也必须是user_name，不可以是userName
     */
    private Integer id;
}
