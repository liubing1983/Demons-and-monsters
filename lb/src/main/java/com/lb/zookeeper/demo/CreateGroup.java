package com.lb.zookeeper.demo;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class CreateGroup extends ZooKeeperInstance {

	// 创建组
	// 参数：groupPath
	public void createPNode(String groupPath) throws KeeperException, InterruptedException {
		// 创建组
		String cGroupPath = zk.create(groupPath, "group".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 输出组路径
		System.out.println("创建的组路径为：" + cGroupPath);
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		CreateGroup cg = new CreateGroup();
		cg.createZKInstance();
		cg.createPNode("/ZKGroupLB");
		cg.ZKclose();
	}
}