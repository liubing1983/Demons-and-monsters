package com.lb.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorDemo {
	
	public static void main(String[] args){
		RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("master1:2181")  // zookeeper服务器列表
				.sessionTimeoutMs(5000)  // 会话超时时间
				.retryPolicy(rp)  // 重试策略
				//.namespace("lb")  // 隔离命名空间
				.build();
		// 启动
		client.start();
		
		try {
			// 创建节点
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/lb", "init".getBytes());
			// 读取子节点
			Stat stat = new Stat();
			System.out.println(new String(client.getData().storingStatIn(stat).forPath("/lb"))+"---");
			System.out.println(stat.getVersion());
			
			client.setData().withVersion(stat.getVersion()).forPath("/lb").getVersion();
			
			System.out.println(new String(client.getData().storingStatIn(stat).forPath("/lb"))+"==");
			System.out.println(stat.getVersion());
			
			client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath("/lb");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
