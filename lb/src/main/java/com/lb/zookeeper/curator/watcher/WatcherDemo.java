package com.lb.zookeeper.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * 
 * @author liubing
 *
 */
public class WatcherDemo extends Thread {

	static String path = "/lb";
	static RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);
	static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("cloud36:2181") // zookeeper服务器列表
			.sessionTimeoutMs(5000) // 会话超时时间
			.retryPolicy(rp) // 重试策略
			// .namespace("lb") // 隔离命名空间
			.build();

	public static void main(String[] args) {

		// 启动
		client.start();

		WatcherDemo wd = new WatcherDemo();
		wd.start();

		try {
			// 创建节点
			// client.delete().deletingChildrenIfNeeded().forPath(path);
			System.out.println("11111111");
			//client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
			
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path+"/a1", "init".getBytes());
			sleep(1000);
			//client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path+"/a2", "init".getBytes());
			sleep(1000);
			//client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path+"/a3", "init".getBytes());
			sleep(1000);
			//client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path+"/a4", "init".getBytes());
			sleep(1000);
			
			sleep(10000);
			System.out.println("创建完成");
			// 读取子节点
			Stat stat = new Stat();
			System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)) + "---");
			System.out.println(stat.getVersion());
			sleep(1000);
			System.out.println("修改 start");
			client.setData().withVersion(stat.getVersion()).forPath(path, "haha".getBytes()).getVersion();
			
			client.setData().forPath(path+"/a1", "haha1".getBytes()).getVersion();
			sleep(1000);
			//client.setData().forPath(path+"/a2", "haha2".getBytes()).getVersion();
			sleep(1000);
			//client.setData().forPath(path+"/a3", "haha3".getBytes()).getVersion();
			sleep(1000);
			//client.setData().forPath(path+"/a4", "haha4".getBytes()).getVersion();
			sleep(1000);
			
			System.out.println("修改  end");
			sleep(5000);
			System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)) + "=====");
			System.out.println(stat.getVersion());
			sleep(5000);
			System.out.println("del");
			
			client.delete().deletingChildrenIfNeeded().forPath(path+"/a1");
			//client.delete().deletingChildrenIfNeeded().forPath(path+"/a2");
			//client.delete().deletingChildrenIfNeeded().forPath(path+"/a3");
			//client.delete().deletingChildrenIfNeeded().forPath(path+"/a4");
			sleep(5000);
			
			client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {

		// 注册监听
		try {
			final NodeCache cache = new NodeCache(client, path, false);
			// 默认为false, 如果设置为ture,nodecache启动时 会立刻加载zookeeper节点上的内容,并缓存在cache中
			cache.start(true);
			cache.getListenable().addListener(new NodeCacheListener() {
				@Override
				public void nodeChanged() throws Exception {
						System.out.println("启动监听");
						sleep(1000);
						System.out.println("节点发生变化:" + cache.getCurrentData().getPath());
						System.out.println("节点发生变化:" + new String(cache.getCurrentData().getData()));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
