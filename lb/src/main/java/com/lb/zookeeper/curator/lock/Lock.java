package com.lb.zookeeper.curator.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Lock {
	static String lock_path = "/lock";
	static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("cloud36:2181").retryPolicy(new ExponentialBackoffRetry(100, 3)).build();
	
	public static void main(String[] args){
		client.start();
		
		final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
		final CountDownLatch down = new CountDownLatch(1);
		
		for(int i = 0; i<= 10; i++){
			new Thread(
					new Runnable() {
						
						@Override
						public void run() {
							try {
								down.await();
								
								// 获取锁
								lock.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							SimpleDateFormat  sdf = new SimpleDateFormat("HHmmss|SSS");
							String s = sdf.format(new Date());
							System.out.println("no:"+s);
							try {
								// 释放锁
								lock.release();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					).start();
		}
		
		down.countDown();
	}

}
