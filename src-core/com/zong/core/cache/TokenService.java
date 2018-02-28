package com.zong.core.cache;

import java.util.UUID;

import com.zong.core.exception.ServiceException;
import com.zong.core.util.ZConst;

/**
 * @desc 用户登录token生成和验证
 * @author zong
 * @date 2017年3月17日
 */
public class TokenService {
	/**
	 * 失效续期时间
	 */
	private long defaultExpiration;
	private CacheService cacheService;

	/**
	 * 根据用户id生成token
	 * 
	 * @param id
	 * @return
	 */
	public String getToken(String id) throws Exception {
		if (id == null) {
			throw new ServiceException("用户id无效");
		}
		// 清空该用户原有的token，重新生成
		if (cacheService.hasKey(ZConst.MODULE_APP_ID_TOKEN, id)) {
			String token = (String) cacheService.get(ZConst.MODULE_APP_ID_TOKEN, id);
			cacheService.delete(ZConst.MODULE_APP_ID_TOKEN, token);
			cacheService.delete(ZConst.MODULE_APP_ID_TOKEN, id);
		}
		// 生成uuid作为token，存到cache缓存服务
		String token = UUID.randomUUID().toString().trim().replaceAll("-", "");
		// 保存用户id和token的关系
		cacheService.put(ZConst.MODULE_APP_ID_TOKEN, id, token);
		cacheService.put(ZConst.MODULE_APP_ID_TOKEN, token, id);
		return token;
	}

	/**
	 * 验证token是否有效
	 * 
	 * @param module
	 * @param token
	 * @throws Exception
	 */
	public String verifyToken(String token) throws Exception {
		if (token == null || token.equals("")) {
			throw new ServiceException("安全令牌格式错误");
		}
		String id = (String) cacheService.get(ZConst.MODULE_APP_ID_TOKEN, token);
		if (id == null) {
			throw new ServiceException("token已过期");
		}
		// 最后一次访问，自动延长失效时间
		if (defaultExpiration != 0) {
			cacheService.expire(ZConst.MODULE_APP_ID_TOKEN, token, defaultExpiration);
			cacheService.expire(ZConst.MODULE_APP_ID_TOKEN, id, defaultExpiration);
		}
		return id;
	}

	/**
	 * 注销token
	 *
	 * @param token
	 */
	public void deleteToken(String token) {
		String id = (String) cacheService.get(ZConst.MODULE_APP_ID_TOKEN, token);
		cacheService.delete(ZConst.MODULE_APP_ID_TOKEN, token);
		cacheService.delete(ZConst.MODULE_APP_ID_TOKEN, id);
	}

	public CacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public long getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

}
