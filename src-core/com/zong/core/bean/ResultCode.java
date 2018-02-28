package com.zong.core.bean;

public class ResultCode {
	public static final int SUCCESS = 200;
	public static final int ERROR = 300;
	// 通用错误以9开头
	public final static int UNKNOW_ERROR = 9999;// 未知错误
	public final static int APP_ERROR = 9001;// 应用级错误
	public final static int SERVICE_ERROR = 9002;// 业务逻辑验证错误
}
