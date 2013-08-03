package com.xunlei.cms.bo;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import com.xunlei.cms.vo.CMSContent;
import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.util.DatetimeUtil;
import com.xunlei.common.web.bean.DataAccessReturn;

@Service("CMSContentService")
@RemoteProxy(name="CMSContentService")
public class CMSContentBoImpl extends DataAccessBo<CMSContent> implements ICMSContentBo{
	public CMSContent test(){
		return new CMSContent();
	}	
	
	@Override
	@RemoteMethod
	public DataAccessReturn<CMSContent> insert(CMSContent data){
		DataAccessReturn<CMSContent> rtn = new DataAccessReturn<CMSContent>();
		data.setInputby(this.currentUserLogo());
		data.setEditby(this.currentUserLogo());
		data.setInputtime(DatetimeUtil.now());
		data.setEdittime(DatetimeUtil.now());
		utilDao.insertObject(data);
		return rtn;
	}
	
	@Override
	@RemoteMethod
	public DataAccessReturn<CMSContent> update(CMSContent data){
		DataAccessReturn<CMSContent> rtn = new DataAccessReturn<CMSContent>();
		data.setEditby(this.currentUserLogo());
		data.setEdittime(DatetimeUtil.now());
		utilDao.updateObject(data);
		return rtn;
	}
}
