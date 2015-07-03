package com.lb.thrift.gis.demo;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


public class GisThriftClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
		TTransport transport = new TFramedTransport(new TSocket("localhost", 7911));
		transport.open();
		
		//使用高密度二进制协议
		TProtocol protocol = new TCompactProtocol(transport);
		
		//创建Client
		GisThrift.Client client = new GisThrift.Client(protocol);
		
		
		long start = System.currentTimeMillis();
		for(int i = 0 ; i<= 10000; i++){
			client.test(i, "tescomm");
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start));
		
		//关闭资源
		transport.close();
	}

}