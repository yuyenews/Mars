package com.mars.netty.server;

import com.mars.core.logger.MarsLogger;
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
public class MarsServer {

	private static MarsLogger log = MarsLogger.getLogger(MarsServer.class);
	
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
			b.childHandler(new MarsServerInitializer());

			/* 服务器绑定端口监听 */
			ChannelFuture f = b.bind(portNumber).sync();
			
			log.info("启动成功");

			f.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("启动netty报错",e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
