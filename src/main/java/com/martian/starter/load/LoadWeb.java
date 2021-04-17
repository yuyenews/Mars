package com.martian.starter.load;

import com.magician.web.MagicianWeb;
import com.martian.annotation.MartianScan;
import com.martian.cache.MartianConfigCache;

/**
 * 加载web
 */
public class LoadWeb {

    private static MagicianWeb magicianWeb;

    public static MagicianWeb getMagicianWeb() {
        return magicianWeb;
    }

    /**
     * 开始加载
     * @throws Exception
     */
    public static void load() throws Exception {

        MartianScan martianScan = MartianConfigCache.getMartianScan();

        magicianWeb = MagicianWeb.createWeb();

        String[] scanPackageList = martianScan.scanPackage();
        if(scanPackageList == null || scanPackageList.length < 1){
            throw new Exception("请设置需要扫描的包");
        }

        for(String packageName : scanPackageList){
            magicianWeb.scan(packageName);
        }
    }
}
