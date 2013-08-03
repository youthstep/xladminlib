package com.xunlei.cms.bo;

import java.util.List;

import com.xunlei.cms.vo.CMSInputType;
import com.xunlei.common.bo.IDataAccessBo;

public interface ICMSInputTypeBo extends IDataAccessBo<CMSInputType>{
	public List<CMSInputType> getCMSInputTypes();
	
	public CMSInputType getCMSInputTypeById(long id);
}
