/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.bo;

import java.util.List;

import com.xunlei.common.bo.IDataAccessBo;
import com.xunlei.libfun.vo.Menus;
import com.xunlei.libfun.vo.UserInfo;


/**
 * 系统菜单模块的业务接口
 * 
 * @author jason
 */
public interface IMenusBo extends IDataAccessBo<Menus>{
	/**
	 * 返回所有菜单,无排序无结构
	 * @return
	 */
	List<Menus> getAllMenus();
	
	/**
	 * 根据用户权限返回可访问的
	 * @param userinfo
	 * @return
	 */
	List<Menus> getAllMenusByUserInfo(UserInfo userinfo);
}
