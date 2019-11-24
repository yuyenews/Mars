package com.mars.core.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局存储空间
 * @author yuye
 *
 */
public class MarsSpace {

	private static MarsSpace constants;
	
	private Map<String, Object> map = new ConcurrentHashMap<>();

	private MarsSpace() {
	}

	public static MarsSpace getEasySpace() {
		if (constants == null) {
			constants = new MarsSpace();
		}

		return constants;
	}
	
	/**
	 * 往Constants里添加数据
	 * @param key 键
	 * @param value 值
	 */
	public void setAttr(String key,Object value) {
		map.put(key, value);
	}
	
	/**
	 * 从Constants里获取数据
	 * @param key 键
	 * @return 值
	 */
	public Object getAttr(String key) {
		return map.get(key);
	}
	
	/**
	 * 移除元素
	 * @param key 键
	 */
	public void remove(String key) {
		map.remove(key);
	}
}