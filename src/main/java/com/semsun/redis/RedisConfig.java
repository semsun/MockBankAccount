package com.semsun.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * redis的配置
 * 
 * @author
 * 
 */
public class RedisConfig {

	private static Log log = LogFactory.getLog(RedisConfig.class);
	
	private static RedisConfig config;
	private Properties properties;
	
	private String redisPoolMaxActive;
	private String redisPoolMaxIdle;
	private String redisPoolMaxWait;
	private String redisPoolTestOnBorrow;
	private String redisPoolTestOnReturn;
	
	private int port;
	private String type;
	private String ip;
	private String database;

	public static RedisConfig getConfig() {
		if (config == null) {
			config = new RedisConfig();
		}
		return config;
	}
	
	public RedisConfig() {
		this.loadPropertiesFromSrc();
	}

	public String getRedisPoolMaxActive() {
		return redisPoolMaxActive;
	}

	public void setRedisPoolMaxActive(String redisPoolMaxActive) {
		this.redisPoolMaxActive = redisPoolMaxActive;
	}

	public String getRedisPoolMaxIdle() {
		return redisPoolMaxIdle;
	}

	public void setRedisPoolMaxIdle(String redisPoolMaxIdle) {
		this.redisPoolMaxIdle = redisPoolMaxIdle;
	}

	public String getRedisPoolMaxWait() {
		return redisPoolMaxWait;
	}

	public void setRedisPoolMaxWait(String redisPoolMaxWait) {
		this.redisPoolMaxWait = redisPoolMaxWait;
	}

	public String getRedisPoolTestOnBorrow() {
		return redisPoolTestOnBorrow;
	}

	public void setRedisPoolTestOnBorrow(String redisPoolTestOnBorrow) {
		this.redisPoolTestOnBorrow = redisPoolTestOnBorrow;
	}

	public String getRedisPoolTestOnReturn() {
		return redisPoolTestOnReturn;
	}

	public void setRedisPoolTestOnReturn(String redisPoolTestOnReturn) {
		this.redisPoolTestOnReturn = redisPoolTestOnReturn;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * 加载配置文件
	 */
	public void loadPropertiesFromSrc() {
		InputStream in = null;
		try {
			log.info("begin load redis.properties");
			in = RedisConfig.class.getClassLoader().getResourceAsStream(
					"redis.properties");
			if (in != null) {
				this.properties = new Properties();
				try {
					this.properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			}
			loadProperties(this.properties);
			log.info("load redis.properties finished");
		} catch (IOException e) {
			log.info("load redis.properties exception " + e.getMessage());

			if (in != null)
				try {
					in.close();
				} catch (IOException ie) {
					log.info("close IOException:" + ie.getMessage());
				}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					log.info("close IOException:" + e.getMessage());
				}
		}
	}

	/**
	 * 匹配属性文件
	 * 
	 * @param pro
	 */
	public void loadProperties(Properties pro) {
		redisPoolMaxActive = pro.getProperty("redis.pool.maxActive").trim();
		redisPoolMaxIdle = pro.getProperty("redis.pool.maxIdle").trim();
		redisPoolMaxWait = pro.getProperty("redis.pool.maxWait").trim();
		redisPoolTestOnBorrow = pro.getProperty("redis.pool.testOnBorrow").trim();
		redisPoolTestOnReturn = pro.getProperty("redis.pool.testOnReturn").trim();
		
		String value = "";
		
		value = pro.getProperty("redis.port");
//		if (!String.isEmpty(value)) {
			port = Integer.parseInt( value.trim() );
//		}
		value = pro.getProperty("redis.type");
//		if (!StringUtil.isEmpty(value)) {
			type = value.trim();
//		}
		value = pro.getProperty("redis.ip");
//		if (!StringUtil.isEmpty(value)) {
			ip = value.trim();
//		}
		value = pro.getProperty("redis.database");
//		if (!StringUtil.isEmpty(value)) {
			database = value.trim();
//		}

	}
}
