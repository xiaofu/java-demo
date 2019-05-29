/**
 * @ProjectName: redis-rmstatestore
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2019年3月5日 上午10:51:50
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.cqvip.runwork.jedis;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
 

import redis.clients.jedis.JedisPoolConfig;

import com.google.common.base.Preconditions;

/**
 * <p></p>
 * @author fulaihua 2019年3月5日 上午10:51:50
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月5日
 * @modify by reason:{方法名}:{原因}
 */
public abstract class JedisClient implements Cloneable
{
	 
	protected JedisPoolConfig poolConfig;
	protected String jedisAddress;
	protected String password;
	protected byte[] redisAppKey;
	protected JedisClient(Configuration conf)
	{
		
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(conf.getInt(JedisConfig.JEDIS_POOL_MAX_TOTAL, 8));
		poolConfig.setMaxIdle(conf.getInt(JedisConfig.JEDIS_POOL_MAX_IDLE, 8));
		poolConfig.setMinIdle(conf.getInt(JedisConfig.JEDIS_POOL_MIN_IDEL, 0));
		poolConfig.setMaxWaitMillis(conf.getLong(
				JedisConfig.JEDIS_POOL_MAX_WAIT_MILLIS,
				BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS));
		poolConfig.setNumTestsPerEvictionRun(conf.getInt(
				JedisConfig.JEDIS_POOL_NUM_TESTS_PER_EVICTION_RUN, -1));
		poolConfig.setTestWhileIdle(conf.getBoolean(
				JedisConfig.JEDIS_POOL_TEST_WHILE_IDLE, true));
		poolConfig.setTestOnBorrow(conf.getBoolean(
				JedisConfig.JEDIS_POOL_TEST_ON_BORROW,
				BaseObjectPoolConfig.DEFAULT_TEST_ON_BORROW));
		poolConfig.setTestOnReturn(conf.getBoolean(
				JedisConfig.JEDIS_POOL_TEST_ON_RETURN,
				BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN));
		poolConfig.setTestOnCreate(conf.getBoolean(
				JedisConfig.JEDIS_POOL_TEST_ON_CREATE,
				BaseObjectPoolConfig.DEFAULT_TEST_ON_CREATE));
		poolConfig.setMinEvictableIdleTimeMillis(conf.getLong(
				JedisConfig.JEDIS_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS, 60000));
		poolConfig.setTimeBetweenEvictionRunsMillis(conf.getLong(
				JedisConfig.JEDIS_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS,
				30000L));
		password=conf.getString(JedisConfig.JEDIS_PASS, null);
		jedisAddress=conf.getString(JedisConfig.JEDIS_ADDRESS, null);
		redisAppKey=conf.getString(JedisConfig.JEDIS_APP_KEY, "Test_").getBytes(Charset.forName("utf-8"));
		Preconditions.checkNotNull(jedisAddress, JedisConfig.JEDIS_ADDRESS +"must be set");
	}
	 /**
     * map数据结构单条存储统一入口
     * @param mapName
     * @param appId
     * @param appStateData
     */
    public abstract void storeDataToMap(byte[] mapName, byte[] appId, byte[] appStateData);

    /**
     * 根据key删除map数据中的k-v
     * @param mapName
     * @param appId
     */
    public  abstract  void removeDataFromMapBykey(byte[] mapName, byte[] appId);
    /**
     * 获取map数据结构的统一方法
     * @param arg0
     * @return
     */
    public abstract Map<byte[], byte[]> loadAllDataFromMap(byte[] arg0);

    /**
     * 根据key获取map中的k-v
     * @param arg0
     * @param arg1
     * @return
     */
    public abstract byte[] loadDataFromMapByKey(byte[] arg0, byte[] arg1);

    
    /**
     * 根据KEY获取数据
     * @param arg0
     * @param arg1
     */
    public abstract void storeByKey(byte[] arg0 ,byte[] arg1);

    /**
     *根据KEY存储数据
     * @param arg0
     * @return version的字节数组
     */
    public abstract byte[] loadByKey(byte[] arg0);

    /**
     * 清空redis中存储的RMStareStore数据
     * @param keys
     */
    public abstract void deleteByKeys(List<byte[]> keys);
    

    /**
     * 判断key是否存在
     * @param arg0
     * @return
     */
    public abstract boolean existKey(byte[] arg0);

    public abstract void close();
    
    public byte[] buildKeyPrefix(byte[] args0)
    {
    	ByteBuffer buffer=ByteBuffer.allocate(args0.length+redisAppKey.length);
		buffer.put(redisAppKey).put(args0);
		buffer.flip();
		return buffer.array();
    }
    
}
