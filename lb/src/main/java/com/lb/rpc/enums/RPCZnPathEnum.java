package com.lb.rpc.enums;

/**
 * rpc server path
 * @author liubing
 *
 */
public enum RPCZnPathEnum {

	thriftpath("/tescomm/rpc/thriftServerIP", "thrift"), nettypath("/tescomm/rpc/nettyServerIP", "netty");

	private String name;
	private String path;

	private RPCZnPathEnum(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public static String toString(String  name) {
		String s = "/null";
		for (RPCZnPathEnum ite : RPCZnPathEnum.values()) {
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
