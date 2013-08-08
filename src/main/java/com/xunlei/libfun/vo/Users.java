package com.xunlei.libfun.vo;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class Users {
	private long seqid;
	private String userlogno;
	private String userpassword;
	private boolean superman;
	private String truename;
	private String bindip;
	private boolean inuse;
	private String tel;
	private String email;
	
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public String getUserlogno() {
		return userlogno;
	}
	public void setUserlogno(String userlogno) {
		this.userlogno = userlogno;
	}
	public boolean isSuperman() {
		return superman;
	}
	public void setSuperman(boolean superman) {
		this.superman = superman;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getBindip() {
		return bindip;
	}
	public void setBindip(String bindip) {
		this.bindip = bindip;
	}
	public boolean isInuse() {
		return inuse;
	}
	public void setInuse(boolean inuse) {
		this.inuse = inuse;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
}
