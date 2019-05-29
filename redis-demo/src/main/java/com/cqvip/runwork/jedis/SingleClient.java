package com.cqvip.runwork.jedis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * <p>
 * 单节点redis部署
 * </p>
 * 
 * @author fulaihua 2019年3月5日 下午12:48:37
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: fulaihua 2019年3月5日
 * @modify by reason:{方法名}:{原因}
 */
public class SingleClient extends JedisClient
{

	JedisPool pool;

	protected SingleClient(Configuration conf)  
	{
		super(conf);
		//uri: jedis://password@host:port
		try
		{
			pool = new JedisPool(poolConfig, new URI(jedisAddress));
		}
		catch (URISyntaxException e)
		{
			throw new RuntimeException(e);
		}
																

	}

	@Override
	public void storeDataToMap(byte[] args0, byte[] appId, byte[] appStateData)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.hset(buildKeyPrefix(args0), appId, appStateData);
		}

	}

	@Override
	public void removeDataFromMapBykey(byte[] args0, byte[] appId)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.hdel(buildKeyPrefix(args0), appId);
		}

	}

	@Override
	public Map<byte[], byte[]> loadAllDataFromMap(byte[] arg0)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hgetAll(buildKeyPrefix(arg0));
		}

	}

	@Override
	public byte[] loadDataFromMapByKey(byte[] arg0, byte[] arg1)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.hget(buildKeyPrefix(arg0), arg1);
		}
	}



	@Override
	public void storeByKey(byte[] arg0, byte[] arg1)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.set(buildKeyPrefix(arg0), arg1);
		}

	}

	@Override
	public byte[] loadByKey(byte[] arg0)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.get(buildKeyPrefix(arg0));
		}
	}

	@Override
	public void deleteByKeys(List<byte[]> keys)
	{
		List<byte[]> prefixKeys=new ArrayList<byte[]>();
		for (int i = 0; i < keys.size(); i++)
		{
			prefixKeys.add(buildKeyPrefix(keys.get(i)));
		}
		try (Jedis jedis = pool.getResource())
		{ 
			jedis.multi().del(prefixKeys.toArray(new byte[0][]));
		}

	}

	@Override
	public boolean existKey(byte[] arg0)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.exists(buildKeyPrefix(arg0));
		}
	}

	@Override
	public void close()
	{
		if (pool != null)
			pool.close();
		pool = null;
	}

}
