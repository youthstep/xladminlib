package com.xunlei.cms.bo;

import java.util.List;

import com.xunlei.cms.vo.CMSTemplate;
import com.xunlei.common.bo.IDataAccessBo;

public interface ICMSTemplateBo extends IDataAccessBo<CMSTemplate>{
	public List<CMSTemplate> getCMSTemplate();
}
