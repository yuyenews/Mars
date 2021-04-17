package com.martian.starter.load;

import com.alibaba.druid.pool.DruidDataSource;
import com.martian.cache.MartianConfigCache;

import java.util.*;

/**
 * 加载JDBC
 */
public class LoadJDBC {

    /**
     * 数据源缓存
     */
    private static Map<String, DruidDataSource> druidDataSourceMap;

    /**
     * 默认数据源缓存
     */
    private static String defaultName;

    public static Map<String, DruidDataSource> getDruidDataSourceMap() {
        return druidDataSourceMap;
    }

    public static String getDefaultName() {
        return defaultName;
    }

    /**
     * 开始加载
     * @throws Exception
     */
    public static void load() throws Exception {
        druidDataSourceMap = new HashMap<>();

        List<Properties> propertiesList = MartianConfigCache.getMartianConfig().jdbcProperties();
        if(propertiesList == null || propertiesList.size() < 1){
            return;
        }
        boolean settingDefault = false;
        for(Properties properties : propertiesList){

            if(properties.getProperty("name") == null){
                throw new Exception("jdbc配置缺少name属性");
            }

            /* 将遍历出来的第一个数据源作为默认数据源 */
            if(settingDefault == false){
                defaultName = properties.get("name").toString();
                settingDefault = true;
            }

            /* 创建数据源 */
            DruidDataSource druidDataSource = initDataSource(properties);
            druidDataSourceMap.put(properties.get("name").toString(), druidDataSource);
        }
    }

    /**
     * 获取 DruidDataSource对象
     *
     * @param dataSource 数据源配置
     * @return DruidDataSource对象
     */
    private static DruidDataSource initDataSource(Properties dataSource) throws Exception {
        DruidDataSource druidDataSource = new DruidDataSource();

        Properties properties = new Properties();

        Set proSet = dataSource.keySet();
        if(proSet == null){
            throw new Exception("jdbc配置中缺少必要的属性");
        }
        for(Object key : proSet){
            properties.put("druid."+key, dataSource.get(key));
        }

        druidDataSource.configFromPropety(properties);
        return druidDataSource;
    }
}
