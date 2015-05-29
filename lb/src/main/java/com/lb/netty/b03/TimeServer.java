package com.lb.netty.b03;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

public class TimeServer {
	
	public void run(int port) {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap b  = new ServerBootstrap().group(bossGroup, workGroup);
		b.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.childHandler(new childChannelHandler());
		
		try {
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
	
	private class childChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new TimeServerHandler());			
		}
		
	}
	
	
	public static void main(String[] args) throws IOException{
		int port = 8080;
		if(args != null  && args.length  > 0){
			port = Integer.parseInt(args[0]);
		}
		new TimeServer().run(port);
	}

}
