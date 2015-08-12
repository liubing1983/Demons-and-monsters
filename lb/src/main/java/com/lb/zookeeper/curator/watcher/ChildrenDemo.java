package com.lb.zookeeper.curator.watcher;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.log4j.Logger;

import com.lb.zookeeper.ZkConnection;

public class ChildrenDemo extends Observable {

	Logger log = Logger.getLogger(ChildrenDemo.class);
	static String path = "/lb";

	// 创建连接
	ZkConnection zkc = new ZkConnection(null, "cloud36,cloud37,cloud38");
	CuratorFramework client = zkc.getZKConnection();

	// 创建线程池
	private Executor executor = Executors.newSingleThreadExecutor();

	FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
		public Integer call() throws InterruptedException {
			ChildrenWatch();
			return Integer.valueOf(0);
		}
	});

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * 启动监控过程
	 */
	public void execute() {
		// 通过线程池启动一个额外的线程加载Watching过程
		executor.execute(task);
	}

	/**
	 * 关闭后的对象无法重新启动
	 * 
	 * @throws IOException
	 */
	public void shutdown() throws IOException {
		executor = null;
	}

	/**
	 * 监控cheng
	 */
	public void ChildrenWatch() {
		// 启动
		client.start();
		final PathChildrenCache cache = new PathChildrenCache(client, path, true);
		try {
			cache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			log.error("监控启动异常!!", e);
			e.printStackTrace();
		}
		log.info("监控已经开启！！");
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED:
					log.info("事件类型：" + event.getType() + ", 节点名称" + new String(event.getData().getData()) + ", 节点路径:" + event.getData().getPath() + ", 节点详细信息：" + event.getData().getStat().toString());
					System.out.println("新增");
					break;
				case CHILD_UPDATED:
					log.info("事件类型：" + event.getType() + ", 节点名称" + new String(event.getData().getData()) + ", 节点路径:" + event.getData().getPath() + ", 节点详细信息：" + event.getData().getStat().toString());
					System.out.println("修改");
					break;
				case CHILD_REMOVED:
					log.info("事件类型：" + event.getType() + ", 节点名称" + new String(event.getData().getData()) + ", 节点路径:" + event.getData().getPath() + ", 节点详细信息：" + event.getData().getStat().toString());
					System.out.println("删除");
					break;
				default:
					break;
				}
			}
		});
	}

	public static void main(String[] args) {
		ChildrenDemo cd = new ChildrenDemo();
		cd.execute();
	}

}
