package com.xunlei.cms.dao;

import java.util.List;

import com.xunlei.cms.vo.CMSContent;
import com.xunlei.cms.vo.CMSInputType;
import com.xunlei.cms.vo.CMSTemplate;
import com.xunlei.cms.vo.CMSUnit;

public interface ICMSDao {
	public CMSTemplate queryOneTemplate(long id);
	public CMSUnit queryOneUnit(long id);
	public CMSInputType queryOneInputType(long id);
	
	/**
	 *  order by displayorder desc ,inputtime desc
	 * @param unitid
	 * @param contentStatus @see com.xunlei.cms.constant.CMSContentStatus
	 * @return
	 */
	public List<CMSContent> queryContentsByUnit(long unitid,int contentStatus);
	
	/**
	 * 默认排序方式 order by displayorder desc ,inputtime desc
	 * @param unitd 
	 * @param contentStatus
	 * @param num
	 * @return
	 */
	public List<CMSContent> queryContents(long unitd, int contentStatus, int num);
	
	/**
	 * order by displayorder desc ,inputtime desc
	 * @param unitd
	 * @param contentStatus
	 * @param num
	 * @return
	 */
	public List<CMSContent> queryContents(long[] unitd, int contentStatus, int num);
	
	public CMSInputType queryCMSInputTypeById(long id);
	public List<CMSInputType> queryCMSInputTypes();
	public List<CMSTemplate> queryCMSTemplate();
	public CMSTemplate queryCMSTemplateById(long id);
	public List<CMSUnit> queryCMSUnits(String flatno);
	public CMSUnit queryCMSUnitById(long id);
}
