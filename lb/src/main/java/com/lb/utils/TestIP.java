package com.lb.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 测试IP和端口是否可以访问
 * @author liubing
 *
 */
public class TestIP {
	
	static Logger log = Logger.getLogger(TestIP.class);
	
	/**
	 * 测试IP和端口是否可以访问
	 * @param ip  待测试的IP
	 * @param port   待测试的端口
	 * @return  测试结果
	 */
	public static boolean testConnectIP(String ip, int port){
		Socket connect = new Socket();  
		boolean res = false;
        try {  
            connect.connect(new InetSocketAddress(ip, port),100);  
            res = connect.isConnected();  
        } catch (IOException e) {
        	log.info("无法连接到:"+ip+":"+port);
            // e.printStackTrace();  
        }finally{  
            try {  
                connect.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
		return res;
	}
	
	/**
	 * 测试一组IP和端口是否可以访问
	 * @param ip
	 * @param port
	 * @return 
	 */
	public static String testConnectIP(String[] ip, int port){
		String test_ip = null;
		for(String s : ip){
			if(testConnectIP(s, port)){
				test_ip= s+":"+port;
				break;
			}
		}
		return test_ip;
	}

	public static void main(String[] args) {
	}

}
