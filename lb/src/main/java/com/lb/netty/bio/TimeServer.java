package com.lb.netty.bio;

import java.io.IOException;

public class TimeServer {
	
	public static void main(String[] args) throws IOException{
		int port = 8080;
		if(args != null  && args.length  > 0){
			port = Integer.parseInt(args[0]);
		}
	}

}
