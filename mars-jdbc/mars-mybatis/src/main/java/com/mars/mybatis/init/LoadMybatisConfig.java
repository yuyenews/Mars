package com.mars.mybatis.init;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.constant.EasySpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.ConfigUtil;
import com.mars.core.util.FileUtil;
import com.mars.mybatis.util.ReadXml;
import com.mars.mybatis.util.extend.MyDataSourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 组装myBatis配置文件
 * @author yuye
 *
 */
public class LoadMybatisConfig {
	
	private static MarsLogger logger = MarsLogger.getLogger(LoadMybatisConfig.class);
	
	private static EasySpace easySpace = EasySpace.getEasySpace();

	/**
	 * 默认配置文件的mapper文件占位符
	 */
	private static final String MAPPERS = "{mappers}";
	/**
	 * 默认配置文件的数据源占位符
	 */
	private static final String ENVIRONMENTS ="{environments}";
	/**
	 * 默认配置文件的默认数据源占位符
	 */
	private static final String DEF = "{def}";

	/**
	 * 获取配置文件并以字符串形式返回
	 * @return str
	 */
	public static String getConfigStr() throws Exception {
		try {
			FileUtil.local = String.valueOf(ConfigUtil.getJdbcConfig().get("config-location"));
			String str = FileUtil.readFileString("/"+FileUtil.local);
			if(str == null) {
				str = defaultConfig();
				str = str.replace(ENVIRONMENTS,getDataSources());
				str = str.replace(MAPPERS,getMappers());
			} else {
				/* 禁止在自定义mybatis配置文件里配置数据源 */
				if(str.indexOf("environment") > -1 || str.indexOf("dataSource") > -1 || str.indexOf("environments") > -1) {
					throw new Exception("不可以在mybatis配置文件里配置数据源");
				}

				/* 禁止在自定义mybatis配置文件里配置mappers */
				if(str.indexOf("mappers") > -1 || str.indexOf("mapper") > -1) {
					throw new Exception("不可以在mybatis配置文件里配置mappers");
				}
				str = str.replace("</configuration>","");
				str = str + "<environments default=\""+DEF+"\">" + getDataSources() + "</environments>";
				str = str + "<mappers>" + getMappers() + "</mappers>";
				str = str+"</configuration>";
			}
			str = str.replace(DEF, easySpace.getAttr("defaultDataSource").toString());
			return str;
		} catch (Exception e) {
			throw new Exception("加载mybatis配置出错",e);
		}
	}
	
	/**
	 * 获取所有mapper文件路径，并组装成xml格式的字符串返回
	 * @return str
	 */
	private static String getMappers() throws Exception {
		try {
			String mappers = ConfigUtil.getJdbcConfig().getString("mappers");
			
			Set<String> xmls = ReadXml.loadXmlList(mappers);
			
			StringBuffer buffer = new StringBuffer();
			for(String str : xmls) {
				buffer.append("<mapper resource=\""+str+"\"/>");
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new Exception("加载mybatis配置文件出错",e);
		}
	}

	/**
	 * 加载数据源配置
	 * @return str
	 */
	private static String getDataSources() throws Exception {
		try {
			String def = "";
			
			JSONArray array = new JSONArray();

			Object dataSources = ConfigUtil.getJdbcConfig().get("dataSource");

			if(dataSources instanceof JSONArray){
				array = (JSONArray)dataSources;
			} else {
				array.add(dataSources);
			}

			StringBuffer dataSource = new StringBuffer()  ;

			List<String> daNames = new ArrayList<>();
			
			for (int i = 0; i < array.size(); i++) {
				
				JSONObject jsonObject = array.getJSONObject(i);
				
				ckDsConfig(jsonObject);
				
				if(i == 0) {
					def = jsonObject.getString("name");
				}
				
				String type = getDataSourceType();
				
				StringBuffer buffer = new StringBuffer();
				buffer.append("<environment id=\""+jsonObject.getString("name")+"\">");
				buffer.append("<transactionManager type=\"JDBC\"/>");
				buffer.append("<dataSource type=\""+type+"\">");
				for(String key : jsonObject.keySet()) {
					if(!key.equals("name") && !key.equals("type")) {
						buffer.append("<property name=\""+key+"\" value=\""+jsonObject.get(key)+"\"/>");
					}
				}
				buffer.append("</dataSource>");
				buffer.append("</environment>");
				dataSource.append(buffer);
				
				daNames.add(jsonObject.getString("name"));
			}
			
			easySpace.setAttr("dataSourceNames", daNames);
			easySpace.setAttr("defaultDataSource", def);
			
			return dataSource.toString();
		} catch (Exception e) {
			throw new Exception("加载mybatis数据源出错",e);
		}
	}
	
	/**
	 * 获取数据源类型
	 * @return str
	 */
	private static String getDataSourceType() {
		return MyDataSourceFactory.class.getName();
	}
	
	/**
	 * 验证数据源配置
	 * @return str
	 */
	private static boolean ckDsConfig(JSONObject jsonObject) throws Exception {
		if(jsonObject.get("name") == null) {
			logger.error("数据源没有指定name");
			throw new Exception("数据源没有指定name");
		}
		return true;
	}
	
	/**
	 * 默认配置
	 * @return str
	 */
	private static String defaultConfig() throws Exception {
		try {

			Object dialect = ConfigUtil.getJdbcConfig().get("dialect");

			if(dialect == null) {
				/* 方言 默认mysql */
				dialect = "mysql";
			}

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			stringBuffer.append("<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">");
			stringBuffer.append("<configuration>");
			stringBuffer.append("<properties>");
			stringBuffer.append("<property name=\"dialect\" value=\""+dialect+"\" />");
			stringBuffer.append("</properties>");
			stringBuffer.append("<plugins>");
			stringBuffer.append("<plugin interceptor=\"com.github.pagehelper.PageHelper\">");
			stringBuffer.append("<property name=\"dialect\" value=\""+dialect+"\" />");
			stringBuffer.append("</plugin>");
			stringBuffer.append("</plugins>");
			stringBuffer.append("<environments default=\""+DEF+"\">");
			stringBuffer.append(ENVIRONMENTS);
			stringBuffer.append("</environments>");
			stringBuffer.append("<mappers>");
			stringBuffer.append(MAPPERS);
			stringBuffer.append("</mappers>");
			stringBuffer.append("</configuration>");

			return stringBuffer.toString();
		} catch (Exception e) {
			throw new Exception("加载mybatis配置文件出错",e);
		}
	}
}
