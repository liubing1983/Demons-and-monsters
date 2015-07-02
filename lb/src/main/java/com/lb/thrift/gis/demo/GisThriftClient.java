package com.lb.thrift.gis.demo;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class GisThriftClient {
	/**
	 * 
	 * @param ip thriftserver IP
	 * @param id 序号
	 * @param name  名称
	 */
	public void startClient(String ip, int id, String name) {
		TTransport transport;
		try {
			transport = new TSocket(ip, 50089);
			TProtocol protocol = new TBinaryProtocol(transport);
			GisThrift.Client client = new GisThrift.Client(protocol);
			transport.open();
			// 执行同步方法
			System.out.println(client.test(id, name));
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GisThriftClient client = new GisThriftClient();
		String ip = "127.0.0.1";
		for (int i = 1; i < 10; i++) {
			client.startClient(ip, i, "tescomm");
		}
	}

}
