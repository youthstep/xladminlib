package com.xunlei.libfun.vo;

import com.xunlei.common.bo.BaseBo;

public class RolePrivilege extends BaseBo {

	private String roleno;
	private long privilegeid;
	
	public String getRoleno() {
		return roleno;
	}
	public void setRoleno(String roleno) {
		this.roleno = roleno;
	}
	public long getPrivilegeid() {
		return privilegeid;
	}
	public void setPrivilegeid(long privilegeid) {
		this.privilegeid = privilegeid;
	}
	public RolePrivilege() {
		// TODO Auto-generated constructor stub
	}

}
