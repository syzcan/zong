package com.zong.core.exception;

import com.zong.core.bean.ResultCode;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "应用异常";

	protected int code = ResultCode.APP_ERROR;

	public AppException() {
		super(MESSAGE);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(int code, String message) {
		super(message);
		this.code = code;
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
