/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.lb.netty.protoc;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author liub
 * @date 2014年2月14日
 * @version 1.0
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

	/**
	 * Creates a client-side handler.
	 */
	public SubReqClientHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		long l = System.currentTimeMillis();
		System.out.println("start : "+ l);
		for (int i = 0; i < 10000; i++) {
			ctx.write(subReq(i));
		}
		System.out.println("end : "+ (System.currentTimeMillis() - l));
		ctx.flush();
	}

	/**
	 * 回调函数
	 * @param i
	 * @return
	 */
	private SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(i);
		builder.setUserName("tescomm"+i);
		builder.setProductName("tescomm"+i);
		builder.setAddress("NanJing YuHuaTai");
		//System.out.println("9-9-09-09-09-0");
		return builder.build();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//System.out.println("Receive server response : [" + msg + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
