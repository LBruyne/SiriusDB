package com.siriusdb.master.zk;

import com.siriusdb.common.UtilConstant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: Region Server信息的观察者
 * @author: liuxuanming
 * @date: 2021/05/09 10:00 下午
 */
public class RegionServerMonitor implements Watcher, AsyncCallback.ChildrenCallback {

    private static final Logger logger = LoggerFactory.getLogger(RegionServerMonitor.class);

    ZooKeeper zk;

    Watcher chainedWatcher;

    RegionServerMonitorListener listener;

    Boolean dead;

    List<String> preServerInfos;

    /**
     * 执行器对该类进行监听需要实现此Listener
     */
    public interface RegionServerMonitorListener {

        /**
         * The existence status of the node has changed.
         */
        void changed(List<String> saIds);

        /**
         * The ZooKeeper session is no longer valid.
         *
         * @param rc
         * the ZooKeeper closing reason code
         */
        void closing(int rc);
    }

    public RegionServerMonitor(ZooKeeper zk, Watcher chainedWatcher, RegionServerMonitorListener listener) {

        this.zk = zk;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;

        /**
         * 监控开始: 获取children节点开始。
         * 设置本对象为监控对象，回调对象也是本对象。
         * 以后均是事件驱动。
         */
        zk.getChildren(UtilConstant.ZNODE, true, this, null);
    }

    /**
     * 拿到Children节点后的回调函数
     *
     * @param rc
     * @param path
     * @param ctx
     * @param children
     */
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {

        boolean exists;
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.getChildren(UtilConstant.ZNODE, true, this, null);
                return;
        }

        List<String> serverInfos = null;

        // 如果存在，再次查询到最新children，此时仅查询，不要设置监控了
        if (exists) {
            try {
                serverInfos = zk.getChildren(UtilConstant.ZNODE, null);
            } catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }

        // 拿到最新saids后，通过listener（executor），加载Saids。
        if ((serverInfos == null && serverInfos != preServerInfos)
                || (serverInfos != null && !serverInfos.equals(preServerInfos))) {
            listener.changed(serverInfos);
            preServerInfos = serverInfos;
        }
    }

    /**
     * 监控回调函数。
     * 如果发生变化，通过getChildren再次监控，并处理Children节点变化后的业务。
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
        if (watchedEvent.getType() == Event.EventType.None) {
            // We are are being told that the state of the connection has changed
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with
                    // server and any watches triggered while the client was
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (path != null && path.equals(UtilConstant.ZNODE)) {
                // Something has changed on the node, let's find out
                zk.getChildren(UtilConstant.ZNODE, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(watchedEvent);
        }
    }
}
