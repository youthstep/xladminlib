package com.xunlei.libfun.bo;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.vo.Privilege;
import com.xunlei.libfun.vo.RolePrivilege;

@Service("privilegeService")
@RemoteProxy(name="privilegeService")
public class PrivilegeBoImpl extends DataAccessBo<Privilege> implements IPrivilegeBo {

	public PrivilegeBoImpl() {
		super();
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Privilege> query(Privilege data, QueryInfo pinfo) {
		return super.query(data, pinfo);
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Privilege> insert(Privilege data) {
		return super.insert(data);
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Privilege> update(Privilege data) {
		return super.update(data);
	}

	@RemoteMethod
	@Override
	public DataAccessReturn<Privilege> deleteSome(Privilege[] objs) {
		for(Privilege r : objs){
			this.utilDao.deleteObjectByCondition(new RolePrivilege(), "privilegeid='" + r.getSeqid() + "'");
		}
		return super.deleteSome(objs);
	}

	@RemoteMethod
	@Override
	public void searchNewServiceMethod() {
		
	}
	
}
