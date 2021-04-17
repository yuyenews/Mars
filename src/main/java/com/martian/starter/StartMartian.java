package com.martian.starter;

import com.alibaba.druid.pool.DruidDataSource;
import com.magician.jdbc.MagicianJDBC;
import com.martian.annotation.MartianScan;
import com.martian.cache.MartianConfigCache;
import com.martian.config.MartianConfig;
import com.martian.starter.load.LoadJDBC;
import com.martian.starter.load.LoadServer;
import com.martian.starter.load.LoadWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 启动类
 */
public class StartMartian {

    private static Logger logger = LoggerFactory .getLogger(StartMartian.class);

    /**
     * 启动服务
     * @param cls
     * @param martianConfig
     */
    public static void start(Class<?> cls, MartianConfig martianConfig){
        try {
            MartianScan martianScan = cls.getAnnotation(MartianScan.class);

            if(martianScan == null){
                throw new Exception("启动类上缺少MartianScan注解");
            }

            MartianConfigCache.setMartianScan(martianScan);
            MartianConfigCache.saveConfig(martianConfig);

            loadJDBC();
            loadWeb();
            loadServer();
        } catch (Exception e){
            logger.error("启动服务失败", e);
        }
    }

    /**
     * 加载服务
     * @throws Exception
     */
    private static void loadServer() throws Exception {
        LoadServer.load();
    }

    /**
     * 加载web
     * @throws Exception
     */
    private static void loadWeb() throws Exception {
        LoadWeb.load();
    }

    /**
     * 加载数据源
     * @throws Exception
     */
    private static void loadJDBC() throws Exception {
        LoadJDBC.load();

        Map<String, DruidDataSource> druidDataSourceMap = LoadJDBC.getDruidDataSourceMap();

        MagicianJDBC magicianJDBC = MagicianJDBC.createJDBC();
        for(String name : druidDataSourceMap.keySet()){
            magicianJDBC.addDataSource(name, druidDataSourceMap.get(name));
        }

        magicianJDBC.defaultDataSourceName(LoadJDBC.getDefaultName());
    }
}
