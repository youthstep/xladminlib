/*
 * UserInfo.java
 *
 * Created on 2006年11月3日, 下午6:13
 *
 */

package com.xunlei.libfun.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.annotations.DataTransferObject;

import com.xunlei.libfun.constant.LoginStatus;

/**
 * 记录登陆后，系统用户的基本信息，权限信息，角色信息。
 * @author Brice Li
 */
@DataTransferObject
public class UserInfo{
    //在session存放的参数名
    public static final String NAME ="userinfo";
    
    /**
     * @see com.xunlei.libfun.constant.LoginStatus
     */
    private int loginStatus = LoginStatus.DEFAULT;
    
    /**
     * 登录信息
     */
    private String loginMsg;

    private Users user;
    
    private Map<String,Privilege> servicePrivilegeMap = new HashMap<String, Privilege>();
    
    private List<String> menunoList = new ArrayList<String>();

    public UserInfo(Users user) {
		super();
		this.user = user;
	}
    
    public List<String> getMenunoList() {
		return menunoList;
	}

	public Map<String, Privilege> getServicePrivilegeMap() {
		return servicePrivilegeMap;
	}
	
	public String getUserlogno() {
		return this.user.getUserlogno();
	}

	public int getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getLoginMsg() {
		return loginMsg;
	}

	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
