package com.mars.netty.server;

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
public class EasyServerInitializer extends ChannelInitializer<SocketChannel> {

	private int timeOut = 10;
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
		ph.addLast("handler", new EasyServerHandler());// 服务端业务逻辑
	}


	private IdleStateHandler getIdleStateHandler(){
		return new IdleStateHandler(timeOut,2000000000,0);
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
		Object timeOuto = jsonObject.get("timeOut");
		Object maxContentLengtho = jsonObject.get("maxContentLength");

		if(timeOuto!=null) {
			timeOut = Integer.parseInt(timeOuto.toString());
		}

		if(maxContentLengtho != null){
			maxContentLength = Integer.parseInt(maxContentLengtho.toString());
		}
	}
}
