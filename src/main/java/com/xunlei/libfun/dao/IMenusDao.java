package com.xunlei.libfun.dao;

import java.util.List;

import com.xunlei.libfun.vo.Menus;

/**
 * @author Brice Li
 *
 */
public interface IMenusDao {
	/**
	 * 获得所有menus,无任何条件判断
	 * 默认按照 displayorder asc
	 * @return
	 */
	List<Menus> getAllMenus();
	
	/**
	 * 获得inuse为ture的菜单
	 * 默认按照 displayorder asc
	 * @return
	 */
	List<Menus> getInuseMenus();
	
	/**
	 * 获得生效的由userlongno过滤的menus
	 * 默认按照 displayorder asc
	 * @param userlogno 用户登录帐号
	 * @return
	 */
	List<Menus> getMenusByUserlongno(String userlogno);
}
