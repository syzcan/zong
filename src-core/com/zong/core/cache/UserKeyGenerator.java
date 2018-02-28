package com.zong.core.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zong.core.util.MD5Util;

/**
 * @desc spring注解缓存，自定义缓存数据 key 生成策略
 * @author zong
 * @date 2016年11月29日 下午10:11:43
 */
public class UserKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object o, Method method, Object... objects) {
		StringBuilder sb = new StringBuilder();
		sb.append(o.getClass().getSimpleName());
		sb.append("_" + method.getName());
		for (Object obj : objects) {
			String key = obj.toString();
			try {
				// 先将参数序列化为json，再将json字符串md5加密
				ObjectMapper mapper = new ObjectMapper();
				key = MD5Util.MD5(mapper.writeValueAsString(obj));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			sb.append("_" + key);
		}
		return sb.toString();
	}

}
