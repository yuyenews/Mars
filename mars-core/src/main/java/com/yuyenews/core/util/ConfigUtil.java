package com.yuyenews.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.logger.GogeLog4jUtil;

/**
 * 配置文件工具类
 * @author yuye
 *
 */
public class ConfigUtil {

	private static EasySpace constants = EasySpace.getEasySpace();


	/**
	 * 加载配置文件
	 */
	public static void loadConfig() throws Exception{
		try {
			/* 读取本地配置文件 */
			String content = FileUtil.readYml(EasyConstant.CONFIG_PATH);
			JSONObject object = JSONObject.parseObject(content);

			/* 从配置中心获取配置信息 */
			JSONObject config = RemoteConfigUtil.remoteConfig(object);

			/* 保证端口号不被修改 */
			config.put("port",object.get("port"));

			/* 将配置信息缓存下来 */
			constants.setAttr("config", config);

			/* 加载log4j配置文件 */
            GogeLog4jUtil.initLog4jConfig(config);
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

	/**
	 * 获取JDBC配置信息
	 *
	 * @return 配置信息
	 */
	public static JSONObject getJdbcConfig() throws Exception {
		try {
			JSONObject jsonObject = getConfig();

			if (jsonObject != null) {

				JSONObject jdbc = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("jdbc")));

				return jdbc;
			}
		} catch (Exception e) {
			throw new Exception("从配置文件中读取jdbc模块配置出错",e);
		}
		return new JSONObject();
	}

}
