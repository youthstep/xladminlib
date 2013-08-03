package com.xunlei.cms.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.cms.dao.ICMSDao;
import com.xunlei.cms.vo.CMSInputType;
import com.xunlei.common.bo.DataAccessBo;

@Service("CMSInputTypeService")
@RemoteProxy(name="CMSInputTypeService")
public class CMSInputTypeBoImpl extends DataAccessBo<CMSInputType> implements ICMSInputTypeBo{
	@Autowired
	private ICMSDao cmsDao;
	
	
	@Override
	@RemoteMethod
	public List<CMSInputType> getCMSInputTypes(){
		return cmsDao.queryCMSInputTypes();
	}
	
	@Override
	@RemoteMethod
	public CMSInputType getCMSInputTypeById(long id){
		return cmsDao.queryCMSInputTypeById(id);
	}
	
	public ICMSDao getCmsDao() {
		return cmsDao;
	}
	
	public void setCmsDao(ICMSDao cmsDao) {
		this.cmsDao = cmsDao;
	}
}
