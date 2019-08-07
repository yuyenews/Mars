package com.mars.netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 定义netty服务
 * @author yuye
 *
 */
public class MarsServerInitializer extends ChannelInitializer<SocketChannel> {

	private int readTimeOut = 10;
	private int writeTimeOut = 2000000000;
	private int maxContentLength = 10485760;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		/* 加载配置文件 */
		getConfig();

		/* 处理http服务的关键handler */
		ChannelPipeline ph = ch.pipeline();
		ph.addLast("idlestatus", getIdleStateHandler());
		ph.addLast("encoder", new HttpResponseEncoder());
		ph.addLast("decoder", new HttpRequestDecoder());
		ph.addLast("aggregator", getHttpObjectAggregator());
		ph.addLast("handler", new MarsServerHandler());// 服务端业务逻辑
	}


	private IdleStateHandler getIdleStateHandler(){
		return new IdleStateHandler(readTimeOut,writeTimeOut,0);
	}

	private HttpObjectAggregator getHttpObjectAggregator(){
		return new HttpObjectAggregator(maxContentLength);
	}

	/**
	 * 超时时间
	 * @return
	 */
	private void getConfig() {

		JSONObject jsonObject = ConfigUtil.getConfig();
		Object timeOut = jsonObject.get("timeOut");
		Object maxContentLength2 = jsonObject.get("maxContentLength");

		if(timeOut != null){
			JSONObject timeOut2 = JSONObject.parseObject(JSON.toJSONString(timeOut));
			Object readTimeOut2 = timeOut2.get("readTimeOut");
			Object writeTimeOut2 = timeOut2.get("writeTimeOut");

			if(readTimeOut2!=null) {
				readTimeOut = Integer.parseInt(readTimeOut2.toString());
			}
			if(writeTimeOut2!=null) {
				writeTimeOut = Integer.parseInt(writeTimeOut2.toString());
			}
		}

		if(maxContentLength2 != null){
			maxContentLength = Integer.parseInt(maxContentLength2.toString());
		}
	}
}
