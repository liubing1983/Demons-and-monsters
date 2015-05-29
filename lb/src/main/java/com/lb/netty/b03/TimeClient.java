package com.lb.netty.b03;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class TimeClient {
	
	public void conect(int port, String host){
		EventLoopGroup group = new NioEventLoopGroup();
		// 配置客户端NIO线程组
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
				public void initChannel(SocketChannel ch){
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});
			// 发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// 退出, 释放NIO线程组
			group.shutdownGracefully();
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		int port = 8080;
		if(args != null  && args.length  > 0){
			port = Integer.parseInt(args[0]);
		}
		new TimeClient().conect(port, "127.0.0.1");
	}

}
