package com.xunlei.common.vo;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

public class XLUserInfo {
	/**
	 * 迅雷的用户内部ID
	 */
	private long userno;
	/**
	 * 身份证号码
	 */
	private String idcardno;
	/**
	 * 邮箱
	 */
	private String mail;
	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 真实姓名
	 */
	private String truename;
	/**
	 * QQ
	 */
	private String qq;

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIdcardno() {
		return idcardno;
	}

	public String getMail() {
		return mail;
	}

	public String getMobile() {
		return mobile;
	}

	public String getQq() {
		return qq;
	}

	public String getTruename() {
		return truename;
	}

	public long getUserno() {
		return userno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public void setUserno(long userno) {
		this.userno = userno;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("--XLUSERINFO").append("\n").append("userno:").append(this.userno).append("\n").append("idcardno:")
				.append(this.idcardno).append("\n").append("mail:").append(this.mail).append("\n").append("mobile:")
				.append(this.mobile).append("\n").append("truename:").append(this.truename).append("\n").append("qq:")
				.append(this.qq).append("\n").append("location:").append(this.location).append("\n");
		return s.toString();

	}
}
