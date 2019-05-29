/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年12月11日 上午10:34:52
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.zookeeper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2017年12月11日 上午10:34:52
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2017年12月11日
 * @modify by reason:{方法名}:{原因}
 */
public class Demo
{

	/**
	 * @author fulaihua 2017年12月11日 上午10:34:52
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		
		String data=DigestAuthenticationProvider.generateDigest("super:superman");
		System.out.println(data);
		
		
		
		CuratorFramework  client = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(1000,5))
				.connectString("vdatanode1,vdatanode2,vnamenode").authorization("digest", "super:1".getBytes())
				.sessionTimeoutMs(15000).build();

		client.start();
		//System.out.println(client.getData().forPath("/test"));
		client.delete().deletingChildrenIfNeeded().forPath("/rmstore");
	}

}
