package com.zong.core.cache;

/**
 * @desc 缓存操作接口，module使用redis实现后面加 : 对应自定义namespace，使用ehcache对应cacheName
 * @author zong
 * @date 2016年12月28日
 */
public interface CacheService {

	public void put(String module, String key, Object value);

	public Object get(String module, String key);

	public void delete(String module, String key);

	public boolean hasKey(String module, String key);

	public void expire(String module, String key, long timeout);
}
