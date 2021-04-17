package com.martian.cache;

import com.martian.annotation.MartianScan;
import com.martian.config.MartianConfig;

/**
 * 配置缓存
 */
public class MartianConfigCache {

    /**
     * 配置类对象
     */
    private static MartianConfig martianConfig;

    /**
     * 要扫描的包
     */
    private static MartianScan martianScan;

    /**
     * 保存配置
     * @param martianConfig
     */
    public static void saveConfig(MartianConfig martianConfig){
        MartianConfigCache.martianConfig = martianConfig;
    }

    /**
     * 获取配置
     * @return
     */
    public static MartianConfig getMartianConfig(){
        return MartianConfigCache.martianConfig;
    }

    public static MartianScan getMartianScan() {
        return martianScan;
    }

    public static void setMartianScan(MartianScan martianScan) {
        MartianConfigCache.martianScan = martianScan;
    }
}
