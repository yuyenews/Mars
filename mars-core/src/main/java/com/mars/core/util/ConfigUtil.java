package com.mars.core.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;

/**
 * 配置文件工具类
 * @author yuye
 *
 */
public class ConfigUtil {

	private static MarsSpace constants = MarsSpace.getEasySpace();


	/**
	 * 加载配置文件
	 * @param suffix 后缀
	 * @throws Exception 异常
	 */
	public static void loadConfig(String suffix) throws Exception{
		try {
			/* 读取本地配置文件 */
			String content = null;
			if(suffix == null){
				content = FileUtil.readYml(MarsConstant.CONFIG_PATH_DEFAULT);
			} else {
				content = FileUtil.readYml(MarsConstant.CONFIG_PATH.replace("{suffix}",suffix));
			}
			JSONObject object = JSONObject.parseObject(content);

			/* 从配置中心获取配置信息 */
			JSONObject config = RemoteConfigUtil.remoteConfig(object);

			/* 保证端口号不被修改 */
			config.put("port",object.get("port"));

			/* 将配置信息缓存下来 */
			constants.setAttr("config", config);

			/* 加载log4j配置文件 */
            MarsLog4jUtil.initLog4jConfig(config);
		} catch (Exception e) {
			throw new Exception("加载配置文件出错",e);
		}
	}


	/**
	 * 获取配置信息
	 * @return json
	 */
	public static JSONObject getConfig() {
		Object obj = constants.getAttr("config");
		if(obj != null) {
			JSONObject jsonObject = (JSONObject)obj;
			
			return jsonObject;
		}
		return null;
	}
}
