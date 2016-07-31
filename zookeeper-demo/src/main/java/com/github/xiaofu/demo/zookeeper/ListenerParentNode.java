package com.github.xiaofu.demo.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * getChildren方法中的观察
 * </p>
 * 
 * @author fulaihua 2014-7-11 下午2:55:38
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-7-11
 * @modify by reason:{方法名}:{原因}
 */
public class ListenerParentNode implements IZkStateListener, IZkDataListener,
		IZkChildListener {

	private boolean isHolder;
	private ZkClient zkClient;
	protected static final Logger LOG = LoggerFactory
			.getLogger(ListenerParentNode.class);

	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setHolder(boolean isHolder) {
		this.isHolder = isHolder;
	}

	public boolean isHolder() {
		return isHolder;
	}

	public ListenerParentNode() {
		// 这里封装了zookeeper对象，一构造zookeeper对象就开始创建连接了，但是不一定创建好了，可能完了，可能没有完成
		zkClient = ZKMgr.getNewInstance();
	}

	protected void createZNode() {

		String resultSatus = ZkUtils.createZNodeIfNotExists(zkClient,
				ZKConstant.PARENT_PATH, null, CreateMode.PERSISTENT);
		if (resultSatus != null) {
			LOG.info("I'm parent leader");
			isHolder = true;
		} else {
			LOG.info("I'm parent follower");
		}
	}

	/**
	 * 
	 * 
	 * @author fulaihua 2014-7-11 下午3:05:29
	 */
	public void startWatcher() {
		// 由于连接速度问题，那么subscribeStateChanges注册的回调可能会收到，可能不会收到KeeperState.SyncConnected状态
		// 如果是先创建节点，再注册事件，handleDataChange就无法回调

		zkClient.unsubscribeAll();
		zkClient.subscribeChildChanges(ZKConstant.PARENT_PATH, this);
		zkClient.subscribeStateChanges(this);
		zkClient.subscribeDataChanges(ZKConstant.PARENT_PATH, this);

		LOG.info(this.getClass().getSimpleName()
				+ "  started watcher to zookeeper for path "
				+ ZKConstant.PARENT_PATH);
		createZNode();
	}

	/**
	 * 如果由于过期而产生了一个新会话会回调此方法，所谓的新会话就是创建一个新的Zookeeper
	 */
	@Override
	public void handleNewSession() throws Exception {
		LOG.info("begin  a new session");
		startWatcher();
	}

	/**
	 * 状态发生改变时会回调此方法，测试，直接把ZK关了，客户端就会不断尝试，就会出现各种状态 断开连接状态，连接成功状态，过期状态等
	 */
	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		System.out.println("I'm changed state:" + state.toString());
	}

	/**
	 * 当前节点创建;当前节点的数据被修改;如果会话过期此方法还要被调用一次
	 * @param dataPath 数据改变节点的绝对路径
	 * @param data 数据
	 */
	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		System.out.println("data changed");
		System.out.println("dataPath:" + dataPath);
		System.out.println("data:" + data);
	}

	/**
	 * 如果当前节点被删除：读不到数据抛异常，所以需要回调此方法;如果会话过期此方法还要被调用一次，前提是节点被删除了
	 * @param dataPath 被删除节点的绝对路径
	 */
	@Override
	public void handleDataDeleted(String dataPath) throws Exception {

		System.out.println("parent node be deleted!");
		System.out.println(dataPath);
		//createZNode();

	}
	/**
	 * 当前节点的创建与删除;当前节点的子节点删除与创建;如果会话过期此方法还要被调用一次
	 * @param parentPath 父节点，一直有值，不会为NULL
	 * @param currentChilds 子节点列表，如果没有子节点，返回一个空集合，若是父节点不存在（被删除等），此参数为NULL
	 */
	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds)
			throws Exception {
		System.out.println("child node changed");
		System.out.println("parentPath:" + parentPath);
		if(currentChilds==null)
		{
			System.out.println("currentChilds is empty");
		}
		else
		{
			System.out.println("currentChilds counts:"+currentChilds.size());
		}
		if (currentChilds != null)
			for (String string : currentChilds) {
				System.out.println(string);
			}

	}

}
