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
	 * 仅包含有效的菜单
	 * @param menunos
	 * @return
	 */
	List<Menus> getMenusByMenunos(List<String> menunos);
}
