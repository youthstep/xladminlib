package com.xunlei.libfun.vo;

public class RoleToPrivilege {
	public long seqid;
	public String roleno;
	public long permissionid;
	
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public String getRoleno() {
		return roleno;
	}
	public void setRoleno(String roleno) {
		this.roleno = roleno;
	}
	public long getPermissionid() {
		return permissionid;
	}
	public void setPermissionid(long permissionid) {
		this.permissionid = permissionid;
	}
}
