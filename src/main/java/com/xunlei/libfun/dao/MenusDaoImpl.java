/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.xunlei.common.dao.BaseDao;
import com.xunlei.libfun.vo.Menus;

/**
 * 系统菜单模块的底层数据访问实现类
 * 
 * @author Brice Li
 */
@Component
public class MenusDaoImpl extends BaseDao implements IMenusDao{

    public MenusDaoImpl(){
        super();
    }
    
	@Override
	public List<Menus> getAllMenus() {
		return this.findObjects(new Menus(), null, "displayorder asc");
	}
	
	@Override
	public List<Menus> getInuseMenus() {
		return this.findObjects(new Menus(), "inuse=" + Menus.INUSE_TRUE, "displayorder asc");
	}

	@Override
	public List<Menus> getMenusByMenunos(List<String> menunos) {
		return this.findObjects(
				new Menus(), 
				"inuse=" + Menus.INUSE_TRUE + " and menuno in ('" + StringUtils.join(menunos, "','") + "')", 
				"");
	}	
}
