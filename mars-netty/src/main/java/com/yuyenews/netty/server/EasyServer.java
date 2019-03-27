package com.yuyenews.netty.server;

import com.yuyenews.core.logger.MarsLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务
 * @author yuye
 *
 */
public class EasyServer {

	private static MarsLogger log = MarsLogger.getLogger(EasyServer.class);
	
	/**
	 * 启动netty服务
	 * @param portNumber
	 */
	public static void start(final int portNumber) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new EasyServerInitializer());

			/* 服务器绑定端口监听 */
			ChannelFuture f = b.bind(portNumber).sync();
			
			log.info("启动结束");
			
			/* 监听服务器关闭监听 */
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("启动netty报错",e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
