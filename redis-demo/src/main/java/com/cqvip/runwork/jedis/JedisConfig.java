package com.cqvip.runwork.jedis;

public class JedisConfig   {
 
    public static final String JEDIS_PREFIX = "jedis.";
    public static final String JEDIS_APP_KEY=JEDIS_PREFIX+"app.key";//每个应用都应该打上一个唯一标识，
    //basic config
    public static final String JEDIS_DEPLOY = JEDIS_PREFIX + "deploy";//CLUSTER,single
    public static final String JEDIS_ADDRESS = JEDIS_PREFIX + "address";//host:port,host:port，separate by comma
    public static final String JEDIS_PASS = JEDIS_PREFIX + "pass";
    public static final String JEDIS_POOL_PREFIX = JEDIS_PREFIX+"pool.";
    //pool config
    public static final String JEDIS_POOL_MAX_TOTAL = JEDIS_POOL_PREFIX + "maxTotal";//maxActive
    public static final String JEDIS_POOL_MAX_IDLE = JEDIS_POOL_PREFIX + "maxIdle";
    public static final String JEDIS_POOL_MIN_IDEL = JEDIS_POOL_PREFIX + "minIdle";
    public static final String JEDIS_POOL_MAX_WAIT_MILLIS = JEDIS_POOL_PREFIX + "maxWaitMillis";
    public static final String JEDIS_POOL_TEST_ON_BORROW = JEDIS_POOL_PREFIX + "testOnBorrow";
    public static final String JEDIS_POOL_TEST_ON_CREATE = JEDIS_POOL_PREFIX + "testOnCreate";
    public static final String JEDIS_POOL_TEST_ON_RETURN = JEDIS_POOL_PREFIX + "testOnReturn";
    public static final String JEDIS_POOL_TEST_WHILE_IDLE= JEDIS_POOL_PREFIX + "testWhileIdle";//true
    public static final String JEDIS_POOL_NUM_TESTS_PER_EVICTION_RUN = JEDIS_POOL_PREFIX + "numTestsPerEvictionRun";
    public static final String JEDIS_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = JEDIS_POOL_PREFIX + "minEvictableIdleTimeMillis";
    public static final String JEDIS_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS = JEDIS_POOL_PREFIX + "timeBetweenEvictionRunsMillis";

    //cluster config
    public static final String JEDIS_CLUSTER_PREFIX = JEDIS_PREFIX+"cluster.";
    public static final String JEDIS_CLUSTER_CONNECTION_TIMEOUT = JEDIS_CLUSTER_PREFIX+"connectionTimeout";//2000
    public static final String JEDIS_CLUSTER_SO_TIMEOUT = JEDIS_CLUSTER_PREFIX+"soTimeout";
    public static final String JEDIS_CLUSTER_MAX_ATTEMPTS = JEDIS_CLUSTER_PREFIX+"masAttempts";//5
    
    
}
