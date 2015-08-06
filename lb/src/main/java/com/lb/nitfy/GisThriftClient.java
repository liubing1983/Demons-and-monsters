package com.lb.nitfy;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.lb.thrift.gis.demo.GisThrift;

public class GisThriftClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// 设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
		TTransport transport = new TFramedTransport(new TSocket("localhost", 50088));

		 TProtocol protocol = new TBinaryProtocol(transport);
		// 创建Client
		GisThrift.Client client = new GisThrift.Client(protocol);
		transport.open();
		long start = System.currentTimeMillis();
		System.out.println(start);
		for (int i = 0; i <= 20000; i++) {
			client.test(1, "tescomm");
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start));

		// 关闭资源
		transport.close();
	}

}