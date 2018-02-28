package com.zong.core.cache.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.zong.core.cache.CacheService;

/**
 * redis缓存实现类，通过redisTemplate.opsForValue存取，保存结构为命名空间 module+":"+key
 * 
 * @author zong
 * 
 */
public class RedisServiceImpl implements CacheService {

	private long defaultExpiration;
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 存储单个对象
	 * 
	 * @param module 所属模块
	 * @param key 保存的key
	 * @param value 保存对象
	 */
	@Override
	public void put(String module, String key, Object value) {
		redisTemplate.opsForValue().set(getModuleKey(module, key), value);
		if (defaultExpiration != 0) {
			redisTemplate.expire(getModuleKey(module, key), defaultExpiration, TimeUnit.SECONDS);
		}
	}

	/**
	 * 删除 对象
	 * 
	 * @param key
	 */
	@Override
	public void delete(String module, String key) {
		redisTemplate.delete(getModuleKey(module, key));
	}

	/**
	 * 获取 缓存对象
	 * 
	 * @param module 所属模块
	 * @param key key
	 * @return 返回值
	 */
	@Override
	public Object get(String module, String key) {
		return redisTemplate.opsForValue().get(getModuleKey(module, key));
	}

	/**
	 * 对象是否存在
	 * 
	 * @param module 所属模块
	 * @param key
	 * @return 存在返回true
	 */
	@Override
	public boolean hasKey(String module, String key) {
		return redisTemplate.hasKey(getModuleKey(module, key));
	}

	@Override
	public void expire(String module, String key, long timeout) {
		redisTemplate.expire(getModuleKey(module, key), timeout, TimeUnit.SECONDS);
	}

	/**
	 * 模块module和key联合作为redis中的key，module后面拼接 : 可以作为namespace
	 */
	private String getModuleKey(String module, String key) {
		return module + ":" + key;
	}

	public long getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
