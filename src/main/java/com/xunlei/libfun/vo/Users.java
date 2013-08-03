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
	private String userlogintype;
	private String tel;
	private String qq;
	private String email;
	private String startvaliddate;
	private String endvaliddate;
	@Deprecated
	private String upuserlogno;
	@Deprecated
	private String workingplatform;
	private boolean singlelogin;
	@Deprecated
	private String editby;
	@Deprecated
	private String edittime;
	
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
	public String getUserlogintype() {
		return userlogintype;
	}
	public void setUserlogintype(String userlogintype) {
		this.userlogintype = userlogintype;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStartvaliddate() {
		return startvaliddate;
	}
	public void setStartvaliddate(String startvaliddate) {
		this.startvaliddate = startvaliddate;
	}
	public String getEndvaliddate() {
		return endvaliddate;
	}
	public void setEndvaliddate(String endvaliddate) {
		this.endvaliddate = endvaliddate;
	}
	public String getUpuserlogno() {
		return upuserlogno;
	}
	public void setUpuserlogno(String upuserlogno) {
		this.upuserlogno = upuserlogno;
	}
	public String getWorkingplatform() {
		return workingplatform;
	}
	public void setWorkingplatform(String workingplatform) {
		this.workingplatform = workingplatform;
	}
	public boolean isSinglelogin() {
		return singlelogin;
	}
	public void setSinglelogin(boolean singlelogin) {
		this.singlelogin = singlelogin;
	}
	public String getEditby() {
		return editby;
	}
	public void setEditby(String editby) {
		this.editby = editby;
	}
	public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
}
