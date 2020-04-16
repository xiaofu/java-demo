/**
 * 
 */
package com.cqvip.runwork.jedis;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author fulaihua
 *
 */
public class JedisPoolTest {

	private JedisPoolConfig poolConfig;
	private JedisPool pool;

	@Before
	public void before() throws URISyntaxException {
		poolConfig = new JedisPoolConfig();
		pool = new JedisPool(poolConfig, new URI("redis://192.168.30.181:7617"));

	}

	/**
	 * 	带上auth命令的密码认证并且key前缀为test，set成功
	 */
	@Test
	public void testSetSuccess() {
		try (Jedis jedis = pool.getResource()) {
			jedis.auth("test1");
			jedis.set("test_abc".getBytes(), "dddd".getBytes());
		}
	}
	
	@Test
	public void testSetClusterFailure() {
		try (Jedis jedis = pool.getResource()) {
			jedis.auth("test");
			jedis.set("test_Test_abc".getBytes(), "dddd".getBytes());
		}
	}
	
	/**
	 * 	不带auth命令，get失败
	 */
	@Test(expected = JedisDataException.class)
	public void testGetFailure() {
		try (Jedis jedis = pool.getResource()) {
			Assert.assertEquals(new String(jedis.get("test_abc".getBytes())),"dddd");
		}
	}
	
	/**
	 * 	带上auth命令的密码认证并且key前缀为test，get成功
	 */
	@Test
	public void testGetSuccess() {
		try (Jedis jedis = pool.getResource()) {
			jedis.auth("test");
			Assert.assertEquals(new String(jedis.get("test_abc".getBytes())),"dddd");
		}
	}
	
	@Test
	public void testMSet() {
		try (Jedis jedis = pool.getResource()) {
			jedis.auth("test");
			jedis.mset("test_test1", "aaa", "test_test2", "bbb");
		}
	}

	/**
	 * 	管线一次发送多个命令
	 */
	@Test
	public void testPipline() {
		try (Jedis jedis = pool.getResource()) {
			jedis.auth("test");
			Pipeline pline = jedis.pipelined();
			pline.append("test_p_append", "p_append");
			pline.incr("test_p_incr");
			pline.mset("test_p_mset_key1","p_mset_value1","test_p_mset_key2","p_mset_value2");
			for (Object item : pline.syncAndReturnAll()) {
				System.out.println("response:"+item);
			}
		}
	}
}
