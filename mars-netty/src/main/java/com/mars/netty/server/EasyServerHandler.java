package com.mars.netty.server;

import com.mars.core.logger.MarsLogger;
import com.mars.netty.thread.RequestThread;
import com.mars.netty.thread.ThreadPool;
import com.mars.netty.util.ResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;

/**
 * 接收netty服务
 * @author yuye
 *
 */
public class EasyServerHandler extends ChannelInboundHandlerAdapter {

	private MarsLogger log = MarsLogger.getLogger(EasyServerHandler.class);

	/**
	 * 接收并处理 客户端请求
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		FullHttpRequest httpRequest = null;
		
		try {
			if (msg instanceof FullHttpRequest) {
				
				httpRequest = (FullHttpRequest) msg;

				/* 用新线程处理请求 */
				RequestThread requestThread = new RequestThread();
				requestThread.setHttpRequest(httpRequest);
				requestThread.setCtx(ctx);
				ThreadPool.execute(requestThread);
			} else {
				ResponseUtil.sendServerError(ctx,"处理请求发生错误");
			}

		} catch (Exception e) {
			log.error("处理请求失败!", e);
			ResponseUtil.sendServerError(ctx,"处理请求发生错误"+e);

			/* 已经通过线程中的finally 释放请求了，所以这里，在出异常的时候，才释放 */
			try {
				ctx.close();
				httpRequest.release();
			} catch (Exception e2) {
			}
		}
	}

	

	/**
	 * 建立连接时，返回消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush("客户端" + InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
		super.channelActive(ctx);
	}

	/**
	 * 超时处理
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent idleStateEvent = (IdleStateEvent)evt;

			switch (idleStateEvent.state()){
				case READER_IDLE:
					ResponseUtil.sendTimeout(ctx,"请求超时");
					break;
				case WRITER_IDLE:
					ResponseUtil.sendTimeout(ctx,"请求超时");
					break;
				default:
					super.userEventTriggered(ctx, evt);
			}
		}
	}
}
