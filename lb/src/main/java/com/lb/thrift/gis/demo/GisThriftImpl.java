package com.lb.thrift.gis.demo;

import org.apache.thrift.TException;

public class GisThriftImpl implements GisThrift.Iface {
	
	

	@Override
	public String test(int id, String name) throws TException {
		
		StringBuffer sb = new StringBuffer();
		for(int i =0 ; i<= 10000; i++){
			sb.append("問世間情爲何物， 指教人生死相許， 天南地北雙飛燕， 老翅幾回寒暑， 歡樂去， 離別苦， 就中更有癡兒女， 君應有語， 渺萬里層雲， 千山暮雪， 隻影向誰去！！");
		}
		
		String s = "";
		for(int i =0 ; i<= 10000; i++){
			s = s +"問世間情爲何物， 指教人生死相許， 天南地北雙飛燕， 老翅幾回寒暑， 歡樂去， 離別苦， 就中更有癡兒女， 君應有語， 渺萬里層雲， 千山暮雪， 隻影向誰去！！";
		}

		// System.out.println("id:"+id+", nema:"+name);
		// 判断用户名是否为tescomm
		if (name.equals("tescomm"))
			return "id:" + id + ", nema:" + name + "--" + sb.toString();
		else
			return "id:" + id + ", name is not tescomm";
	}

}
