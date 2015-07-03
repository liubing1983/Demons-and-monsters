package com.lb.zookeeper.curator.example;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;

/**
 * 
 * @author liubing
 *
 */
public class MonitorZNode extends Thread{
	
	private static final String MONITOR_PATH = "/tescommzk/monitor";
	private static final String JOB_PATH = "/tescommzk/job";
	
	public static void main(String[] args) throws Exception {
		
		// 启动线程防止监听停止
		MonitorZNode monitor = new MonitorZNode();
		monitor.start();
		
		
		String zkConnString = "cloud38:2181";
		CuratorFramework client = null;
		// 监控monitor节点
		TreeCache monitor_cache = null;
		// 监控job节点
		TreeCache job_cache = null;
		try {
			// 连接zk
			client = CuratorFrameworkFactory.newClient(zkConnString, new ExponentialBackoffRetry(1000, 3));
			client.start();

			// 配置监听monitor
			System.out.println("开始监听monitor节点!");
			monitor_cache = new TreeCache(client, MONITOR_PATH);
			monitor_cache.start();
			//processCommands(client, monitor_cache);
			addListener(monitor_cache, "monitor");
			
			// 注册临时子节点
			System.out.println("把自己注册到zk");
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(MONITOR_PATH+"/a1/aa/bb/cc", "init".getBytes());
			
			// 配置监听job
			System.out.println("开始监听job节点!");
			job_cache = new TreeCache(client, JOB_PATH);
			job_cache.start();
			addListener(job_cache, "job");
			
			
		} finally {
			CloseableUtils.closeQuietly(monitor_cache);
			CloseableUtils.closeQuietly(job_cache);
			CloseableUtils.closeQuietly(client);
		}
	}

	private static void addListener(final TreeCache cache, String type) {
		TreeCacheListener listener = new TreeCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				switch (event.getType()) {
				case NODE_ADDED: {
					System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: " + new String(event.getData().getData()));
					event.getData().getStat().getCversion();
					break;
				}
				case NODE_UPDATED: {
					System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: " + new String(event.getData().getData()));
					break;
				}
				case NODE_REMOVED: {
					System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}
				default:
					System.out.println("Other event: " + event.getType().name());
				}
			}

		};
		cache.getListenable().addListener(listener);
	}
	
	public void run(){
		while(true){
			try {
				sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
