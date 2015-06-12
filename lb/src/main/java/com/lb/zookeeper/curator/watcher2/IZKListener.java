package com.lb.zookeeper.curator.watcher2;

import org.apache.curator.framework.CuratorFramework;

public interface IZKListener {
    void executor(CuratorFramework client);
}
