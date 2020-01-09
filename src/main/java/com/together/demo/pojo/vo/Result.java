package com.together.demo.pojo.vo;

import com.together.demo.utils.TogetherConstants;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author maxiaoguang
 */
public class Result extends HashMap<String, Object> implements Serializable {

	public Result() {
		put("code", 0);
	}

	public static Result error() {
		return error(-1, "未知异常");
	}

	public static Result error(String msg) {
		return error(-1, msg);
	}

	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}
	public static Result error(Object data) {
		Result r = new Result();
		r.put("msg", TogetherConstants.OPERATION_FAIL);
		r.put("data",data);
		return r;
	}
	public static Result ok(Object data) {
		Result r = new Result();
		r.put("msg", TogetherConstants.OPERATION_SUCCESS);
		r.put("data",data);
		return r;
	}

	public static Result ok(String msg,  Object data) {
		Result r = new Result();
		r.put("msg", msg);
		r.put("data",data);
		return r;
	}

	public static Result ok() {
		Result r = new Result();
		r.put("msg", TogetherConstants.OPERATION_SUCCESS);
		return r;
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}