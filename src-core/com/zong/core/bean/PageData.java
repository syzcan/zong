package com.zong.core.bean;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc 说明：参数封装Map，查询参数均使用该类封装
 * @author zong
 * @date 2016年3月18日
 */
@SuppressWarnings("rawtypes")
public class PageData extends DataMap {
	private static final long serialVersionUID = 1L;

	/**
	 * 通过request对象将请求参数进行封装
	 * 
	 * @param request
	 */
	public PageData(HttpServletRequest request) {
		Map properties = request.getParameterMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			put(name, value);
		}
	}

	public PageData() {
	}

	public PageData(Object key, Object value) {
		put(key, value);
	}

	public PageData put(Object key, Object value) {
		super.put(key, value);
		return this;
	}
}