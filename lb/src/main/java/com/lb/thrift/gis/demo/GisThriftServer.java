package com.lb.thrift.gis.demo;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.lb.thrift.gis.demo.GisThrift.Processor;

public class GisThriftServer {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startServer() {
        try {
            TServerSocket serverTransport = new TServerSocket(50089);

            GisThrift.Processor process = new Processor(new GisThriftImpl());

            Factory portFactory = new TBinaryProtocol.Factory(true, true);

            Args args = new Args(serverTransport);
            args.processor(process);
            args.protocolFactory(portFactory);

            TServer server = new TThreadPoolServer(args);
            System.out.println("port 50089 start!!");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	GisThriftServer server = new GisThriftServer();
        server.startServer();
    }

}
