package com.yuyenews.easy.netty.server;

import com.yuyenews.core.logger.GogeLogger;
import com.yuyenews.core.util.MesUtil;
import com.yuyenews.easy.netty.thread.RequestThread;
import com.yuyenews.easy.netty.thread.ThreadPool;
import com.yuyenews.easy.server.request.HttpResponse;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;

/**
 * 接收netty服务
 * @author yuye
 *
 */
public class EasyServerHandler extends ChannelHandlerAdapter {

	private GogeLogger log = GogeLogger.getLogger(EasyServerHandler.class);

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
				sendBad(ctx,"处理请求发生错误");
			}

		} catch (Exception e) {
			log.error("处理请求失败!", e);
			sendBad(ctx,"处理请求发生错误"+e);

			/* 已经通过线程中的finally 释放请求了，所以这里，在出异常的时候，才释放 */
			try {
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
					sendTimeout(ctx,"请求超时");
					break;
				case WRITER_IDLE:
					sendTimeout(ctx,"请求超时");
					break;
				default:
					super.userEventTriggered(ctx, evt);
			}
		}
	}


	/**
	 * 响应
	 * @param ctx
	 */
	private void sendBad(ChannelHandlerContext ctx,String ex){
		HttpResponse response = new HttpResponse(ctx);
		response.send(MesUtil.getMes(500,ex).toJSONString(), HttpResponseStatus.BAD_REQUEST);
	}

	/**
	 * 响应请求超时
	 * @param ctx
	 */
	private void sendTimeout(ChannelHandlerContext ctx,String ex){
		HttpResponse response = new HttpResponse(ctx);
		response.send(MesUtil.getMes(503,ex).toJSONString(), HttpResponseStatus.BAD_REQUEST);
	}
}
