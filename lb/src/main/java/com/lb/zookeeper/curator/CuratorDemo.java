package com.lb.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.lb.zookeeper.ZkConnection;

public class CuratorDemo {
	
	Logger logger = Logger.getLogger(CuratorDemo.class);
	
	// 创建连接
	ZkConnection zkc = new ZkConnection("lb", "cloud36,cloud37,cloud38");
	CuratorFramework client = zkc.getZKConnection();
	
	public void crudDemo() {
		// 启动
		client.start();

		try {
			logger.info("开始创建节点");
			// 创建节点
			client.create().forPath("/a");
			// 创建节点， 并附加初始值
			client.create().forPath("/b", "init_b".getBytes());
			// 创建临时节点
			client.create().withMode(CreateMode.EPHEMERAL).forPath("/c");
			// 创建临时节点， 并附加初始值, 自动同时递归创建父节点
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/d", "init_d".getBytes());
			// 读取子节点
			logger.info("读取节点数据");
			Stat stat = new Stat();
			System.out.println("a:"+new String(client.getData().storingStatIn(stat).forPath("/a")));
			System.out.println("b:"+new String(client.getData().storingStatIn(stat).forPath("/b")));
			System.out.println("c:"+new String(client.getData().storingStatIn(stat).forPath("/c")));
			System.out.println("d:"+new String(client.getData().storingStatIn(stat).forPath("/d")));
			System.out.println(stat.getVersion());

			logger.info("修改节点数据");
			client.setData().withVersion(stat.getVersion()).forPath("/a", "update_a".getBytes()).getVersion();
			client.setData().withVersion(stat.getVersion()).forPath("/b", "update_b".getBytes()).getVersion();
			client.setData().withVersion(stat.getVersion()).forPath("/c", "update_c".getBytes()).getVersion();
			client.setData().withVersion(stat.getVersion()).forPath("/d", "update_d".getBytes()).getVersion();

			System.out.println("a:"+new String(client.getData().storingStatIn(stat).forPath("/a")));
			System.out.println("b:"+new String(client.getData().storingStatIn(stat).forPath("/b")));
			System.out.println("c:"+new String(client.getData().storingStatIn(stat).forPath("/c")));
			System.out.println("d:"+new String(client.getData().storingStatIn(stat).forPath("/d")));
			System.out.println(stat.getVersion());
			//Thread.sleep(30000);
			logger.info("删除节点");
			client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath("/a");
			client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath("/b");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CuratorDemo  cd = new CuratorDemo();
		cd.crudDemo();
	}

}
