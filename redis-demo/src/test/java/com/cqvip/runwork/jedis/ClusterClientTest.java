package com.cqvip.runwork.jedis;

import static org.junit.Assert.*;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

 

public class ClusterClientTest
{
	private static JedisClient client;
	
	@BeforeClass
	public static  void before()
	{
		Configuration conf=new CombinedConfiguration();
		conf.addProperty(JedisConfig.JEDIS_DEPLOY, "CLUSTER");
		conf.addProperty(JedisConfig.JEDIS_ADDRESS, "192.168.30.181:7617");
		client=JedisClientFactory.getJedisClient(conf);
	}
	@Test
	public void testStoreDataToMap()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDataFromMapBykey()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLoadAllDataFromMap()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLoadDataFromMapByKey()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testStoreByKey()
	{
		client.storeByKey("abc".getBytes(), "efg".getBytes());
	}

	@Test
	public void testLoadByKey()
	{
		System.out.println(client.loadByKey("abc".getBytes()));
	}

	@Test
	public void testDeleteByKeys()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testExistKey()
	{
	
	}

	@Test
	public void testClose()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testClusterClient()
	{
		fail("Not yet implemented");
	}

}
