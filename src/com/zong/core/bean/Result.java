package com.zong.core.bean;

import com.zong.core.exception.ServiceException;

public class Result extends DataMap {
	private static final long serialVersionUID = 1L;

	public static final String SUCCESS_MSG = "操作成功";
	public static final String ERROR_MSG = "操作失败";
	public static final String RET_CODE = "retCode";
	public static final String RET_MSG = "retMsg";

	public Result() {
	}

	public Result(Object key, Object value) {
		put(key, value);
	}

	public Result put(Object key, Object value) {
		super.put(key, value);
		return this;
	}

	public Result error(Exception e) {
		put(RET_CODE, ResultCode.ERROR);
		put(RET_MSG, "系统错误，请联系管理员");
		return this;
	}

	public Result error(ServiceException e) {
		put(RET_CODE, e.getCode());
		put(RET_MSG, e.getMessage());
		return this;
	}

	public static Result success() {
		return new Result(RET_CODE, ResultCode.SUCCESS).put(RET_MSG, SUCCESS_MSG);
	}

	public static Result error() {
		return new Result(RET_CODE, ResultCode.ERROR).put(RET_MSG, ERROR_MSG);
	}
}