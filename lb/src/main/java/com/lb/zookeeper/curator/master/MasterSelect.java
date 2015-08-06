package com.lb.zookeeper.curator.master;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class MasterSelect extends Thread{
	
	static String master_path = "/master_path";
	
	CuratorFramework client = CuratorFrameworkFactory.builder().connectString("cloud36:2181").retryPolicy(new ExponentialBackoffRetry(100, 3)).build();
	
	public void run(){
        
		client.start();
		LeaderSelector selector = new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
			
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println("master "+ Thread.currentThread().getName());
				
				Thread.sleep(3000);
			}
		});
		
		selector.autoRequeue();
	
		selector.start();
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		MasterSelect  m1 = new MasterSelect(); 
		MasterSelect  m2 = new MasterSelect(); 
		MasterSelect  m3 = new MasterSelect(); 
		m1.start();
		m2.start();
		m3.start();
	}
	
	

}
