package com.xunlei.cms.bo;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CMSGenerateService")
@RemoteProxy(name="CMSGenserateService")
public class CMSGenerateBoImpl implements ICMSGenerateBo{
	@Autowired
	private ICMSBo cmsBo;

	@Override
	@RemoteMethod
	public void processByUnit(long unitId) throws Exception{
		cmsBo.processByUnit(unitId);
	}
	
	public ICMSBo getCmsBo() {
		return cmsBo;
	}
	
	public void setCmsBo(ICMSBo cmsBo) {
		this.cmsBo = cmsBo;
	}
}
