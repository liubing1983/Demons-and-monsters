package com.lb.filewatch.javawatcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.lb.zookeeper.ZkConnection;

/**
 * 使用java7 filewatch监控目录中,文件的新增, 修改, 删除
 * 
 * @author liubing
 *
 */
public class WatcherServer extends Observable {

	private WatchService watcher;
	private Path path;  // 监控的目录
	private WatchKey key;  // 监控的事件
	private Executor executor = Executors.newSingleThreadExecutor();  // 线程池, java 观察者模式

	FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
		public Integer call() throws InterruptedException {
			processEvents();
			return Integer.valueOf(0);
		}
	});

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * 注册监控事件
	 * @param dir 需要监控的目录
	 * @throws IOException
	 */
	public WatcherServer(String dir) throws IOException {
		watcher = FileSystems.getDefault().newWatchService();
		// 监控目录内文件的更新、创建和删除事件
		path = Paths.get(dir);
		key = path.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);
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
		watcher.close();
		executor = null;
	}

	/**
	 * 监控文件系统事件
	 */
	void processEvents() {
		while (true) {
			// 等待直到获得事件信号
			WatchKey signal;
			try {
				signal = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event : signal.pollEvents()) {
				Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();

				if (kind == ENTRY_CREATE) {
					System.out.println("创建文件： " + name.toString());
				} else if (kind == ENTRY_DELETE) {
					System.out.println("删除文件： " + name.toString());
				} else if (kind == ENTRY_MODIFY) {
					System.out.println("修改文件： " + name.toString());
				}
			}
			// 为监控下一个通知做准备
			key.reset();
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			// 监控目录
			WatcherServer ws = new WatcherServer("/opt/lb");
			ws.execute();

			// 注册zookeeper
			ZkConnection zkc = new ZkConnection("lb", "cloud136,cloud137,cloud138");
			CuratorFramework client = zkc.getZKConnection();
			client.start();
			client.create().withMode(CreateMode.EPHEMERAL).forPath("/filewatcher", "filewatcher".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
