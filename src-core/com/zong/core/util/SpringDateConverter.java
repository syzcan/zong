package com.zong.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @desc spring日期转换器，在springmvc中配置可以自动将页面的时间字符串转换为Date注入到bean，不配置表单提交日期报400错误
 * @author zong
 * @date 2017年3月10日
 */
public class SpringDateConverter implements Converter<String, Date> {
	@Override
	public Date convert(String source) {
		// 常用几种时间转换，先按精确到时分秒解析，不匹配则按年月日解析
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy/MM/dd");
		// 按严格解析
		dateFormat1.setLenient(false);
		dateFormat2.setLenient(false);
		dateFormat3.setLenient(false);
		dateFormat4.setLenient(false);
		// 依次解析匹配
		Date date = parseDate(dateFormat1, source);
		if (date == null) {
			date = parseDate(dateFormat2, source);
		}
		if (date == null) {
			date = parseDate(dateFormat3, source);
		}
		if (date == null) {
			date = parseDate(dateFormat4, source);
		}
		return date;
	}

	private Date parseDate(SimpleDateFormat dateFormat, String source) {
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			return null;
		}
	}
}
