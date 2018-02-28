package com.zong.core.cache.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import com.zong.core.cache.CacheService;

public class EhcacheServiceImpl implements CacheService {

	private EhCacheCacheManager cacheManager;

	public void put(String cacheName, String key, Object value) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(key, value);
	}

	public Object get(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		ValueWrapper vw = cache.get(key);
		return vw == null ? null : vw.get();
	}

	public void delete(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.evict(key);
	}

	public boolean hasKey(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName);
		ValueWrapper vw = cache.get(key);
		return vw != null;
	}

	/**
	 * 没实现失效时间延长
	 */
	public void expire(String module, String key, long timeout) {
		// TODO Auto-generated method stub，没实现失效时间延长
	}

	public EhCacheCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
