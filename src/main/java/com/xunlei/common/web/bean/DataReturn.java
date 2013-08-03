package com.xunlei.common.web.bean;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class DataReturn extends AbstractReturn {
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
