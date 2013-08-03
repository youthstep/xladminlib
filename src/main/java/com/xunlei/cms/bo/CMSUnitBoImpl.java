package com.xunlei.cms.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.cms.dao.ICMSDao;
import com.xunlei.cms.vo.CMSUnit;
import com.xunlei.common.bo.DataAccessBo;

@Service("CMSUnitService")
@RemoteProxy(name="CMSUnitService")
public class CMSUnitBoImpl extends DataAccessBo<CMSUnit> implements ICMSUnitBo{
	@Autowired
	private ICMSDao cmsDao;
	
	public CMSUnitBoImpl(){
		super();
	}
	
	@Override
	@RemoteMethod
	public List<CMSUnit> getCMSUnits(String flatno){
		return cmsDao.queryCMSUnits(flatno);
	}
	
	@Override
	@RemoteMethod
	public CMSUnit getCMSUnitById(long id){
		return cmsDao.queryCMSUnitById(id);
	}
	
	public ICMSDao getCmsDao() {
		return cmsDao;
	}
	
	public void setCmsDao(ICMSDao cmsDao) {
		this.cmsDao = cmsDao;
	}
}
