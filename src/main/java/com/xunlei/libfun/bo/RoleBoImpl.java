package com.xunlei.libfun.bo;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.vo.Role;
import com.xunlei.libfun.vo.RolePrivilege;
import com.xunlei.libfun.vo.UsersRole;

@Service("roleService")
@RemoteProxy(name="roleService")
public class RoleBoImpl extends DataAccessBo<Role> implements IRoleBo{
	
	public RoleBoImpl(){
		super();
	}
	
	@RemoteMethod
	@Override
	public DataAccessReturn<Role> query(Role data, QueryInfo pinfo) {
		return super.query(data, pinfo);
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Role> insert(Role data) {
		int count = this.utilDao.getRecordCount(Role.class, "no='" + data.getNo() + "'");
		if(count > 0){
			return this.genDataAccessReturn(DataAccessReturn.ERROR, "已存在相同得角色编号:" + data.getNo());
		}else{
			return super.insert(data);
		}
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Role> update(Role data) {
		Role tRole = this.utilDao.findObject(data);
		if(tRole.getNo().equals(data.getNo())){
			return super.update(data);
		}else{
			return this.genDataAccessReturn(DataAccessReturn.ERROR, "角色编号不可以更改");
		}
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Role> deleteSome(Role[] objs) {
		for(Role r : objs){
			this.utilDao.deleteObjectByCondition(new UsersRole(), "roleno='" + r.getNo() + "'");
			this.utilDao.deleteObjectByCondition(new RolePrivilege(), "roleno='" + r.getNo() + "'");
		}
		return super.deleteSome(objs);
	}
}
