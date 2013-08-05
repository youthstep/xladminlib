/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.dao.IMenusDao;
import com.xunlei.libfun.vo.Menus;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 系统菜单模块的业务逻辑实现层
 * 
 * @author jason
 */
@Service("menusService")
@RemoteProxy(name="menusService")
public class MenusBoImpl extends DataAccessBo<Menus> implements IMenusBo {
    @Autowired
	public IMenusDao menusDao;
	
	@Override
	public List<Menus> getAllMenus() {
		return menusDao.getAllMenus();
	}

	@Override
	public List<Menus> getAllMenusByUserInfo(UserInfo userinfo) {
		if(userinfo.getUser().isSuperman()){
			return menusDao.getInuseMenus();
		}else {
			return menusDao.getMenusByMenunos(userinfo.getMenunoList());
		}
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Menus> query(Menus data, QueryInfo pinfo) {
		// TODO Auto-generated method stub
		return super.query(data, pinfo);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Menus> insert(Menus data) {
		// TODO Auto-generated method stub
		return super.insert(data);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Menus> update(Menus data) {
		// TODO Auto-generated method stub
		return super.update(data);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Menus> deleteSome(Menus[] objs) {
		// TODO Auto-generated method stub
		return super.deleteSome(objs);
	}
}
