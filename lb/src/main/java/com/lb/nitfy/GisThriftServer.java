package com.lb.nitfy;

import org.apache.thrift.transport.TServerSocket;

import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.lb.thrift.gis.demo.GisThrift;
import com.lb.thrift.gis.demo.GisThrift.Processor;
import com.lb.thrift.gis.demo.GisThriftImpl;

public class GisThriftServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TServerSocket serverTransport = new TServerSocket(50088);
			GisThrift.Processor process = new Processor(new GisThriftImpl());
			
			// Build the server definition
			ThriftServerDef serverDef = new ThriftServerDefBuilder().withProcessor(process).build();
			// Create the server transport
			final NettyServerTransport server = new NettyServerTransport(serverDef);
			// Create netty boss and executor thread pools
			// Start the server
			//server.start(bossExecutor, workerExecutor);
			server.start();
			System.out.println("thrift start, port: 50088");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
