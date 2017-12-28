package com.semsun.redis;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * 基于Redis列表
 * @author rb
 *
 */
public class RedisList {
	
	private long size = 0;
	
	private String key;
	
	private Jedis redis = RedisCache.getRedisCache().getResource();
	
	public RedisList(String key) {
		this.key = key;
		
		if( redis.exists(key) ) {
			size = redis.llen(key);
		}
	}
	
	/**
	 * 增加内容
	 * @param item
	 */
	public void add(String item) {
		redis.lpush(key, item);		// 头部添加
//		redis.lpush(key, item);		// 尾部添加
	}
	
	/**
	 * 获取列表，支持分页
	 * @param key		列表Key
	 * @param pageSize	分页大小，为 0 时，查询所有记录
	 * @param pageNum	分页页码
	 * @return
	 */
	public List<String> getList(int pageSize, int pageNum) {
		int start = (pageNum - 1) * pageSize;
		int end = start + pageSize;
		
		/* 查询所有 */
		if( pageSize == 0 ) {
			return redis.lrange(key, 0, size);
		}
		
		return redis.lrange(key, start, end);
	}
	
	/**
	 * 获取指定序号内容
	 * @param index
	 * @return
	 */
	public String get(int index) {
		if( index >= size ) return null;
		
		return redis.lindex(key, index);
	}
	
	/**
	 * 修改指定序号内容
	 * @param index
	 * @param item
	 */
	public void set(int index, String item) {
		if( index >= size ) return;
		
		redis.lset(key, index, item);
	}

}
