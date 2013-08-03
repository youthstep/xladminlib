package com.xunlei.cms.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.cms.dao.ICMSDao;
import com.xunlei.cms.vo.CMSTemplate;
import com.xunlei.common.bo.DataAccessBo;

@Service("CMSTemplateService")
@RemoteProxy(name="CMSTemplateService")
public class CMSTemplateBoImpl extends DataAccessBo<CMSTemplate> implements ICMSTemplateBo{
	@Autowired
	private ICMSDao cmsDao;
	
	public CMSTemplateBoImpl(){
		super();
	}
	
	@Override
	@RemoteMethod
	public List<CMSTemplate> getCMSTemplate(){
		return cmsDao.queryCMSTemplate();
	}
	
	public ICMSDao getCmsDao() {
		return cmsDao;
	}
	
	public void setCmsDao(ICMSDao cmsDao) {
		this.cmsDao = cmsDao;
	}

}
