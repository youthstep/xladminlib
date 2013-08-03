package com.xunlei.libfun.bo;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.xunlei.common.web.bean.DataReturn;
import com.xunlei.libfun.vo.Menus;
import com.xunlei.libfun.vo.UserInfo;

/**
 * @author liheng
 * @since 2011-10-27 上午11:19:36
 */
public interface ICommonBo {
	public DataReturn getConfig();
	public UserInfo login(String username,String password,String verifyCode);
	public UserInfo hasLogin();
	public void logout();
	public List<Menus> getMenus();
}
