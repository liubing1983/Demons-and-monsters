package com.lb.zookeeper.demo;

import java.io.IOException;
import java.util.List;
 
import org.apache.zookeeper.KeeperException;
 
public class ListMembers extends ZooKeeperInstance {
	public void list(String groupPath) throws KeeperException, InterruptedException{
		//获取所有子节点
		List<String> children=zk.getChildren(groupPath, false);
		if(children.isEmpty()){
			System.out.println("组"+groupPath+"中没有组成员存在！");
			System.exit(1);
		}
		for(String child:children)
			System.out.println(child);		
	}
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException{
		ListMembers lm=new ListMembers();
		lm.createZKInstance();
		lm.list("/ZKGroupLB");
	}
}
