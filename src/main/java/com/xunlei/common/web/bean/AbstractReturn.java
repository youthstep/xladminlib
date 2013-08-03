package com.xunlei.common.web.bean;


public abstract class AbstractReturn {
	private int code = 0;
	private String msg = "";
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
