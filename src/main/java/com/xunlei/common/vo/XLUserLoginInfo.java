package com.xunlei.common.vo;

/**
 * 
 * @author tanxiaohan
 * @since 2012-11-6
 */
public class XLUserLoginInfo {
	/**
	 * 用户旧字母帐号
	 */
	private String usrname;

	/**
	 * 用户新数字帐号
	 */
	private String sernewno;

	/**
	 * 用户内部ID
	 */
	private long userid;

	/**
	 * 用户有效帐号类型 0:旧帐号 1：新帐号
	 */
	private int usertype;

	/**
	 * 用户的登录方式 0: 旧帐号登录 1: 新帐号登录
	 */
	private int logintype;

	/**
	 * 0: 不升级用户， 1: 升级用户
	 */
	private int upgrade;

	/**
	 * 用户昵称
	 */
	private String nickname;

	/**
	 * 用户积分
	 */
	private long score;

	/**
	 * 用户全球排名
	 */
	private long order;

	/**
	 * vip等级 0，1，2，3，4，5，6
	 */
	private int isvip;

	/**
	 * 验证用户合法身份的key
	 */
	private String sessionid;

	public XLUserLoginInfo(String sessionid, long userid) {
		this.sessionid = sessionid;
		this.userid = userid;
	}

	public int getIsvip() {
		return isvip;
	}

	public int getLogintype() {
		return logintype;
	}

	public String getNickname() {
		return nickname;
	}

	public long getOrder() {
		return order;
	}

	public long getScore() {
		return score;
	}

	public String getSernewno() {
		return sernewno;
	}

	public String getSessionid() {
		return sessionid;
	}

	public int getUpgrade() {
		return upgrade;
	}

	public long getUserid() {
		return userid;
	}

	public int getUsertype() {
		return usertype;
	}

	public String getUsrname() {
		return usrname;
	}

	public void setIsvip(int isvip) {
		this.isvip = isvip;
	}

	public void setLogintype(int logintype) {
		this.logintype = logintype;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public void setSernewno(String sernewno) {
		this.sernewno = sernewno;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public void setUpgrade(int upgrade) {
		this.upgrade = upgrade;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public void setUsrname(String usrname) {
		this.usrname = usrname;
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("usrname: ").append(usrname).append("\n").append("sernewno: ").append(sernewno).append("\n")
				.append("userid: ").append(userid).append("\n").append("usertype: ").append(usertype).append("\n")
				.append("logintype: ").append(logintype).append("\n").append("upgrade: ").append(upgrade).append("\n")
				.append("nickname: ").append(nickname).append("\n").append("score: ").append(score).append("\n")
				.append("order: ").append(order).append("\n").append("sessionid: ").append(sessionid).append("\n")
				.append("isvip: ").append(isvip);
		return info.toString();

	}

}
