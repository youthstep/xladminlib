package com.xunlei.libfun.vo;

import com.xunlei.common.vo.BaseVO;

public class UsersRole extends BaseVO {

	private long seqid;
	public long getSeqid() {
		return seqid;
	}

	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}

	private String username;
	private String roleno;
	
	public UsersRole(String username, String roleno) {
		super();
		this.username = username;
		this.roleno = roleno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleno() {
		return roleno;
	}

	public void setRoleno(String roleno) {
		this.roleno = roleno;
	}

	public UsersRole() {
		// TODO Auto-generated constructor stub
	}

}
