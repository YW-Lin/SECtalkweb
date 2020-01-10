package com.wchat.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResultBean {

	private String code;

	private String msg;

	private Object data;

	public ResultBean() {
	}

	public ResultBean(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
