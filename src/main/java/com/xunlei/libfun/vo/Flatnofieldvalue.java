package com.xunlei.libfun.vo;

import java.io.Serializable;
/**
* 此实体类用于保存根据配置文件从业务表中查询出来的平台编号和平台名称
* @author 李亚俊
* 2008-12-11 15:08:02
*
*/
public class Flatnofieldvalue implements Serializable{
	private String flatno = "";
	private String flatname = "";
	public String getFlatno() {
		return flatno;
	}
	public void setFlatno(String flatno) {
		this.flatno = flatno;
	}
	public String getFlatname() {
		return flatname;
	}
	public void setFlatname(String flatname) {
		this.flatname = flatname;
	}
	


}
