package com.lb.nitfy;

import org.apache.thrift.transport.TServerSocket;

import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.lb.thrift.gis.demo.GisThrift;
import com.lb.thrift.gis.demo.GisThrift.Processor;
import com.lb.thrift.gis.demo.GisThriftImpl;

public class GisThriftServer2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//TServerSocket serverTransport = new TServerSocket(50088);
			GisThrift.Processor process = new Processor(new GisThriftImpl());
			
			// Build the server definition
			ThriftServerDef serverDef = new ThriftServerDefBuilder().listen(50088).withProcessor(process).build();
			// Create the server transport
			final NettyServerTransport server = new NettyServerTransport(serverDef);
			server.start();
			System.out.println("thrift start, port: 50088");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
