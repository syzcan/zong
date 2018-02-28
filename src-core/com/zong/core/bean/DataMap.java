package com.zong.core.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class DataMap extends HashMap implements Map, Serializable {
	private static final long serialVersionUID = 1L;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Object get(Object key) {
		Object obj = null;
		if (super.get(key) instanceof Object[]) {
			obj = (Object[]) super.get(key);
		} else {
			obj = super.get(key);
		}
		return obj;
	}

	public String getString(Object key) {
		Object object = get(key);
		if (object != null) {
			return object.toString();
		}
		return null;
	}

	/**
	 * 返回当前对象，可以链式调用
	 */
	@SuppressWarnings("unchecked")
	public DataMap put(Object key, Object value) {
		super.put(key, value);
		return this;
	}

	/**
	 * 使用jackson序列化为json
	 * 
	 * @return json
	 */
	public String toJson() {
		String json = "";
		try {
			json = objectMapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getStringJson(Object key) {
		Object object = get(key);
		if (object != null) {
			String json = "";
			try {
				json = objectMapper.writeValueAsString(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
		return null;
	}

	public DataMap readJsonValue(String key) {
		String obj = getString(key);
		if (obj != null) {
			try {
				return objectMapper.readValue(obj, DataMap.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<DataMap> readJsonValues(String key) {
		String obj = getString(key);
		if (obj != null && !"".equals(obj)) {
			try {
				return objectMapper.readValue(obj, new TypeReference<List<DataMap>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
