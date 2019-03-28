package com.yuyenews.mybatis.init;

import com.yuyenews.core.constant.EasyConstant;
import com.yuyenews.core.constant.EasySpace;
import com.yuyenews.core.logger.MarsLogger;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 加载
 * @author yuye
 *
 */
public class LoadSqlSessionFactory {
	
	private MarsLogger logger = MarsLogger.getLogger(LoadSqlSessionFactory.class);
	
	private static LoadSqlSessionFactory factory;
	
	private String resource;
	
	private static EasySpace easySpace = EasySpace.getEasySpace();

	/**
	 * 单例，防止重复加载配置文件
	 */
	private LoadSqlSessionFactory() {}
	
	/**
	 * 获取本类的对象
	 * @return obj
	 */
	public static LoadSqlSessionFactory getLoadSqlSessionFactory() {
		
		if(factory == null) {
			factory = new LoadSqlSessionFactory();
			factory.loadConfig();
			factory.loadSqlSessionFactorys();
		}
		
		return factory;
	}
	
	/**
	 * 将配置文件字符串转换成输入流
	 */
	private void loadConfig() {
		try {
			resource = LoadMybatisConfig.getConfigStr();
		} catch (Exception e) {
			logger.error("加载配置文件出错",e);
		}
	}
	
	/**
	 * 加载sqlSessionFactory
	 */
	private void loadSqlSessionFactorys() {
		List<String> daNames = (List<String>) easySpace.getAttr("dataSourceNames");
		
		Map<String,SqlSessionFactory> maps = new HashMap<>();
		for(String str : daNames) {
			InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
			maps.put(str, new SqlSessionFactoryBuilder().build(inputStream,str));
		}
		easySpace.setAttr(EasyConstant.DATA_SOURCE_MAP, maps);
	}
	
	/**
	 * 获取sqlSession                                   
	 * @return session
	 */
	public SqlSession getSqlSession() {
		return getSqlSession(null,true);
	}
	
	/**
	 * 获取sqlSession                                   
	 * @return session
	 */
	public SqlSession getSqlSession(String dataSourceName,Boolean autoCommit) {
		Map<String,SqlSessionFactory> maps = (Map<String,SqlSessionFactory>) easySpace.getAttr(EasyConstant.DATA_SOURCE_MAP);
		if(dataSourceName == null) {
			Object defDa = easySpace.getAttr("defaultDataSource");
			return maps.get(defDa.toString()).openSession(autoCommit);
		}
		return maps.get(dataSourceName).openSession(autoCommit);
	}
}
