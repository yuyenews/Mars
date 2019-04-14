package com.mars.netty.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
import com.mars.netty.util.ResponseUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.*;

/**
 * 线程池,用于处理并发
 * @author yuye
 *
 */
public class ThreadPool {

	private static  BlockingQueue<Runnable> workQueue;

	private static ThreadPoolExecutor pool;

	private static int corePoolSize = 100;

	private static int maximumPoolSize = 1000;

	private static int keepAliveTime = 60;

	/**
	 * 新增请求的线程
	 * @param command
	 */
	public static void execute(Runnable command) {
		if(pool == null){
			init();
			workQueue = new ArrayBlockingQueue<>(maximumPoolSize - corePoolSize);
			pool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize, keepAliveTime,TimeUnit.SECONDS,workQueue,new MarsRejectedExecutionHandler());
		}
		pool.execute(command);
	}

	/**
	 * 读取线程池的配置
	 */
	private static void init(){
		JSONObject jsonObject = ConfigUtil.getConfig();
		Object obj = jsonObject.get("threadPool");
		if(obj != null){
			JSONObject threadPool = JSONObject.parseObject(JSON.toJSONString(obj));

			Object cs = threadPool.get("corePoolSize");
			Object mp = threadPool.get("maximumPoolSize");
			Object kt = threadPool.get("keepAliveTime");

			if(cs != null){
				corePoolSize = Integer.parseInt(cs.toString());
			}
			if(mp != null){
				maximumPoolSize = Integer.parseInt(mp.toString());
			}
			if(kt != null){
				keepAliveTime = Integer.parseInt(kt.toString());
			}
		}
	}
}

