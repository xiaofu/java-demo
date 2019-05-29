package com.cqvip.runwork.jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import com.google.common.io.Closeables;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;



/**
 * 
 * <p>
 * 集群模式,本身就自带故障转移!!!!
 * </p>
 * @author fulaihua 2019年3月5日 下午12:48:41
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月5日
 * @modify by reason:{方法名}:{原因}
 */
public class ClusterClient extends JedisClient
{
	private JedisCluster jedis;

	public ClusterClient(Configuration conf)
	{
		super(conf);
		Set<HostAndPort> nodes=new HashSet<>();
		String[] addresses=jedisAddress.split(",");
		for (int i = 0; i < addresses.length; i++)
		{
			String[] address= addresses[i].split(":");
			HostAndPort model=new HostAndPort(address[0], Integer.parseInt(address[1]));
			nodes.add(model);
		}
		
		jedis=new JedisCluster(nodes,conf.getInt(JedisConfig.JEDIS_CLUSTER_CONNECTION_TIMEOUT, 2000),conf.getInt(JedisConfig.JEDIS_CLUSTER_SO_TIMEOUT, 0),conf.getInt(JedisConfig.JEDIS_CLUSTER_MAX_ATTEMPTS, 5),password,poolConfig);

	}

	@Override
	public void storeDataToMap(byte[] args0, byte[] appId, byte[] appStateData)
	{
		 
		jedis.hset(buildKeyPrefix(args0), appId, appStateData);

	}

	@Override
	public void removeDataFromMapBykey(byte[] args0, byte[] appId)
	{
		 jedis.hdel(buildKeyPrefix(args0), appId);

	}

	@Override
	public Map<byte[], byte[]> loadAllDataFromMap(byte[] arg0)
	{
		return jedis.hgetAll(buildKeyPrefix(arg0));
	    
	}

	@Override
	public byte[] loadDataFromMapByKey(byte[] arg0, byte[] arg1)
	{
		return jedis.hget(buildKeyPrefix(arg0), arg1);
	     
	}


	@Override
	public void storeByKey(byte[] arg0, byte[] arg1)
	{
		 jedis.set(buildKeyPrefix(arg0), arg1);

	}

	@Override
	public byte[] loadByKey(byte[] arg0)
	{
		return jedis.get(buildKeyPrefix(arg0));
	       
	}

	@Override
	public void deleteByKeys(List<byte[]> keys)
	{
		for (byte[] arg0 : keys){
            jedis.del(buildKeyPrefix(arg0));
        }
	}

	@Override
	public boolean existKey(byte[] arg0)
	{
		return jedis.exists(buildKeyPrefix(arg0));
	}

	@Override
	public void close()
	{
		if(jedis==null)
			Closeables.closeQuietly(jedis);
		jedis=null;

	}

}
