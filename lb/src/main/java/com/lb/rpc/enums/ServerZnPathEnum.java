package com.lb.rpc.enums;

public enum ServerZnPathEnum {
	
	thriftserver("/tescomm/server", "thrift"), nettyserver("/tescomm/server/", "netty");
	
	private String name;
	private String path;

	private ServerZnPathEnum(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public static String toString(String  name) {
		String s = "/null";
		for (ServerZnPathEnum ite : ServerZnPathEnum.values()) {
			if (name == ite.getName()) {
				s = ite.getPath();
				break;
			}
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	

}
