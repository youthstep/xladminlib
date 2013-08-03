package com.xunlei.cms.bo;

import java.util.List;

import com.xunlei.cms.vo.CMSUnit;
import com.xunlei.common.bo.IDataAccessBo;

public interface ICMSUnitBo extends IDataAccessBo<CMSUnit>{
	public List<CMSUnit> getCMSUnits(String flatno);
	
	public CMSUnit getCMSUnitById(long id);
}
