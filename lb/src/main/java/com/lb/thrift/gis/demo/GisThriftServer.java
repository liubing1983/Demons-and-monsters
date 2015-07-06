package com.lb.thrift.gis.demo;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportFactory;

public class GisThriftServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//传输通道 - 非阻塞方式
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(50088);
			
			//异步IO，需要使用TFramedTransport，它将分块缓存读取。
			TTransportFactory transportFactory = new TFramedTransport.Factory();
			
			//使用高密度二进制协议
			TProtocolFactory proFactory = new TCompactProtocol.Factory();
			
			//设置处理器 WqImpl
			TProcessor processor = new GisThrift.Processor(new GisThriftImpl());
			
			//创建服务器
			TServer server = new TThreadedSelectorServer(
					new Args(serverTransport)
					.protocolFactory(proFactory).selectorThreads(60).workerThreads(20)
					.transportFactory(transportFactory)
					.processor(processor)
				);
			
			System.out.println("Start server on port 7911...");
            server.serve();  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
