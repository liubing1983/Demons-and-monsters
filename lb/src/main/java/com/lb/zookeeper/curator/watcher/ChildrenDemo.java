package com.lb.zookeeper.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ChildrenDemo {

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

		final PathChildrenCache cache = new PathChildrenCache(client, path, true);
		try {
			cache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(true){
			System.out.println("等待子节点----");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				
				for (ChildData c : cache.getCurrentData()){
					System.out.println(new String(c.getData()));
					System.out.println(c.getPath());
					System.out.println(c.getStat().toString());
					System.out.println("ppppppppppppppppppppppppppp");
				}
				if(cache.getCurrentData().isEmpty()){
				System.out.print("发现变化: 节点路径: "+new String(event.getData().getData()));
				switch (event.getType()) {
				case CHILD_ADDED:
					System.out.println("新增");
					break;

				case CHILD_UPDATED:
					System.out.println("修改");
					break;

				case CHILD_REMOVED:
					System.out.println("删除");  
					break;
				default:
					break;
				}
			}
			}
		});
		}
	}

}
