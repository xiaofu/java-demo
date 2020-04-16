package com.cqvip.runwork.jedis;

import static org.junit.Assert.*;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

public class SingleClientTest {
	private static JedisClient client;

	@BeforeClass
	public static  void before() {
		Configuration conf=new CombinedConfiguration();
		conf.addProperty(JedisConfig.JEDIS_DEPLOY, "SINGLE");
		conf.addProperty(JedisConfig.JEDIS_ADDRESS, "redis://192.168.30.181:7617");
		client=JedisClientFactory.getJedisClient(conf);
	}
	
	@Test
	public void testLoadByKey()
	{
	System.out.println(new String(client.loadByKey("abc".getBytes())));
	}
	
	@Test
	public void testStoreByKey()
	{
		client.storeByKey("abc".getBytes(), "aaaa".getBytes());
	}
	

}
