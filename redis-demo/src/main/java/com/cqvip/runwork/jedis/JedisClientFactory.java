/**
 * @ProjectName: redis-rmstatestore
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年3月5日 上午10:49:07
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.runwork.jedis;

 

import org.apache.commons.configuration.Configuration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2019年3月5日 上午10:49:07
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月5日
 * @modify by reason:{方法名}:{原因}
 */
public class JedisClientFactory
{

	public static JedisClient getJedisClient(Configuration conf)
	{
		
		String deploy = conf.getString(JedisConfig.JEDIS_DEPLOY);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(deploy),
				JedisConfig.JEDIS_DEPLOY + " must be set");
		switch (RedisDeploy.valueOf(deploy.toUpperCase()))
		{
		case CLUSTER:
			return new ClusterClient(conf);
		case SINGLE:
			return new SingleClient(conf);
		}
		return null;
	}
}
