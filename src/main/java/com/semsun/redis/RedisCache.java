package com.semsun.redis;

import java.util.ResourceBundle;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
public class RedisCache {
	
	private static JedisPool pool = null;
	
	public static JedisPool getRedisCache() {
		if( null == pool ) {
			InitPoolConfig();
		}
		return pool;
	}

	public static void InitPoolConfig() {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(bundle
				.getString("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(bundle
				.getString("redis.pool.maxIdle")));
		config.setMaxWaitMillis(Long.valueOf(bundle
				.getString("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(bundle
				.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle
				.getString("redis.pool.testOnReturn")));

//		int count = Integer.valueOf(bundle.getString("redis.count"));

		String port = "";
//		String type = "";
		String ip = "";
		String database = "";

		port = bundle.getString("redis.port");
//		type = bundle.getString("redis.type");
		ip = bundle.getString("redis.ip");
		database = bundle.getString("redis.database");

		pool = new JedisPool(config, ip, Integer.valueOf(port),
				Protocol.DEFAULT_TIMEOUT, null, Integer.valueOf(database));

	}

}
