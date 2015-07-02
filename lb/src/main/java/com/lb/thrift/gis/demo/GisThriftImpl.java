package com.lb.thrift.gis.demo;

import org.apache.thrift.TException;

public class GisThriftImpl implements GisThrift.Iface {

	@Override
	public String test(int id, String name) throws TException {
		System.out.println("id:"+id+", nema:"+name);
		// 判断用户名是否为tescomm
		if(name.equals("tescomm"))
			return "id:"+id+", nema:"+name;
		else
		return "id:"+id+", name is not tescomm";
	}
	

}
