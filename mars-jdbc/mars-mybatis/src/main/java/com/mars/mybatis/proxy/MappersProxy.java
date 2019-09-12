package com.mars.mybatis.proxy;

import com.mars.core.annotation.DataSource;
import com.mars.core.constant.MarsSpace;
import com.mars.core.util.ThreadUtil;
import com.mars.mybatis.init.LoadSqlSessionFactory;
import com.mars.jdbc.base.BaseJdbcProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 代理类
 * @author yuye
 *
 */
public class MappersProxy extends BaseJdbcProxy implements MethodInterceptor {
	
	private MarsSpace marsSpace = MarsSpace.getEasySpace();

	private LoadSqlSessionFactory loadSqlSessionFactory = LoadSqlSessionFactory.getLoadSqlSessionFactory();
	
	/**
	 * 获取代理对象
	 * @param clazz  bean的class
	 * @return 对象
	 */
	@Override
	public Object getProxy(Class<?> clazz) {
		Enhancer enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}


	/**
	 * 绑定代理
	 * @param o
	 * @param method
	 * @param args
	 * @param methodProxy
	 * @return obj
	 * @throws Throwable
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

		if(args != null && args.length > 1){
			throw new Exception("MarsDAO的方法只允许有一个参数");
		}

		/* 获取当前线程中的sqlSession */
		Object obj =  marsSpace.getAttr(ThreadUtil.getThreadIdToTraction());
		
		/* 用来执行sql的sqlSession */
		SqlSession session = null;
		
		/* 是否需要手动关闭sqlSession（默认不需要） */
		Boolean flag = false;
		
		/* 返回数据 */
		Object result = null;
		
		/* 获取数据源名称 */
		String dataSourceName = getDataSourceName(method);
		
		if(obj != null) {
			/* 如果当前线程中有sqlSession 则从当前线程中获取sqlSession */
			Map<String,SqlSession> sqlSessions = (Map<String,SqlSession>)obj;
			session = sqlSessions.get(dataSourceName);
		} else {
			/* 否则 手动获取（这种情况，当执行完以后需要手动关闭sqlSession） */
			session = loadSqlSessionFactory.getSqlSession(dataSourceName, false);
			flag = true;
		}
		
		/* 获取要执行的sql的ID */
		String statement = method.getName();
		
		/* 根据要执行的sql的ID 获取这条sql的类型是select还是update */
		SqlCommandType tag = session.getConfiguration().getMappedStatement(statement).getSqlCommandType();
		String commType = tag.toString().toUpperCase();

		if(commType.equals("SELECT")) {
			/* 如果是select，则判断方法的返回值是不是list */
			result = select(method,args,session,statement);
		} else if(commType.equals("UPDATE") || commType.equals("INSERT") || commType.equals("DELETE")) {
			/* 如果要执行的sql是update类型（增删改），则执行update方法 */
			result = update(session,args,statement,flag);
		}
		
		if(flag) {
			/* 手工关闭sqlSession 节约回收的开销 */
			session.close();
		}
		
		return result;
	}

	/**
	 * 查询
	 * @param method
	 * @param args
	 * @param session
	 * @param statement
	 * @return
	 */
	private Object select(Method method,Object[] args,SqlSession session,String statement) {
		Object result = null;
		Class<?> returnType = method.getReturnType();
		if(returnType.getName().equals(List.class.getName())) {
			/* 如果方法的返回值是list，则执行selectList方法 */
			if(args != null && args.length > 0 && args[0] != null) {
				result = session.selectList(statement, args[0]);
			} else {
				result = session.selectList(statement);
			}
		} else {
			/* 如果不是list，则执行selectOne方法 */
			if(args != null && args.length > 0 && args[0] != null) {
				result = session.selectOne(statement, args[0]);
			} else {
				result = session.selectOne(statement);
			}
		}
		return result;
	}

	/**
	 * 增删改
	 * @param session
	 * @param args
	 * @param statement
	 * @param flag
	 * @return
	 */
	private Object update(SqlSession session,Object[] args,String statement,boolean flag){
		Object result = null;
		if(args != null && args.length > 0 && args[0] != null) {
			result = session.update(statement, args[0]);
		} else {
			result = session.update(statement);
		}

		if(flag) {
			/* 如果sqlSession是手动获取的，那么执行完以后要立刻提交事务 */
			session.commit();
		}
		return result;
	}
	
	/**
	 * 获取数据源名称
	 * @param method
	 * @return str
	 */
	private String getDataSourceName(Method method) {
		String dataSourceName = null;
		DataSource dataSource = method.getAnnotation(DataSource.class);
		if(dataSource != null) {
			/* 如果dao的方法上有DataSource注解，则使用注解中的数据源名称 */
			dataSourceName = dataSource.value();
		} else {
			/* 否则使用默认数据源名称 */
			dataSourceName = marsSpace.getAttr("defaultDataSource").toString();
		}
		return dataSourceName;
	}
}
