package com.lb.netty.b03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TimeServerHandler  extends ChannelHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf buf = (ByteBuf)msg;
		
		byte[] req = new byte[buf.readableBytes()];
		
		buf.readBytes(req);
		String s = "";
		try {
			s = new String(req, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("接收到消息: "+ s);
		
		String currentTime = "LB".equalsIgnoreCase(s) ? new Date(System.currentTimeMillis()).toString() : "ERROR";
		
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();
	}

}
