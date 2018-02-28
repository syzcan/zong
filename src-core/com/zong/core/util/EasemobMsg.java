package com.zong.core.util;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.zong.core.bean.PageData;

/**
 * 环信即时云操作类
 * 
 * @author zong
 * 
 */
public class EasemobMsg {
	public static final String API_URL = "https://a1.easemob.com";
	public static final String ORG_NAME = "zong";
	public static final String APP_NAME = "goddess";
	public static final String APP_KEY = "zong#goddess";
	public static final String CLIENT_ID = "YXA6xUxeQA1QEeaHZGPsOfpYdg";
	public static final String CLIENT_SECRET = "YXA6TgK2kByBBh7B8fL108sXNcHz7eY";
	public static final String API_URL_TOKEN = API_URL + "/" + ORG_NAME + "/" + APP_NAME + "/token";

	public static void main(String[] args) {
		try {
			PageData pd = new PageData("grant_type", "client_credentials").put("client_id", CLIENT_ID)
					.put("client_secret", CLIENT_SECRET);
			Response response = Jsoup.connect(API_URL_TOKEN).timeout(60 * 1000).ignoreContentType(true)
					.requestBody(pd.toJson()).method(Method.POST).execute();
			System.out.println(response.body());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
