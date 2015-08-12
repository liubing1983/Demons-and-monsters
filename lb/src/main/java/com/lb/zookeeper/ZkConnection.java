package com.lb.zookeeper;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.lb.utils.TestIP;

public class ZkConnection {
	
	Logger logger = Logger.getLogger(ZkConnection.class);

	private String namespace;
	private String ip;
	private int port;

	/**
	 * 
	 * @param namespace  命名空间
	 * @param ip  zk server IP
	 */
	public ZkConnection(String namespace, String ip) {
		this.namespace = namespace;
		this.ip = ip;
		this.port = 2181;
	}

	/**
	 * 
	 * @param namespace 命名空间
	 * @param ip   zk server IP
	 * @param port    zk  server  端口
	 */
	public ZkConnection(String namespace, String ip, int port) {
		this.namespace = namespace;
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 创建zookeeper连接
	 * @return
	 */
	public CuratorFramework getZKConnection() {
		String zk_ip = TestIP.testConnectIP(this.ip.split(","), port);
		if (StringUtils.isEmpty(zk_ip)) {
			logger.error("无法连接到zookeeper, 请检查IP和PORT.  IP:" + ip + "; PORT:" + port, new RuntimeException());
			System.exit(0);
		}
		logger.info("准备连接到: " + zk_ip);

		// 创建zookeeper连接
		if (StringUtils.isBlank(namespace)) {
			return CuratorFrameworkFactory.builder().connectString(zk_ip).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(100, 3)).build();
		} else {
			return CuratorFrameworkFactory.builder().connectString(zk_ip).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(100, 3)).namespace(namespace).build();
		}
	}

	public static void main(String[] args) {

	}
}
