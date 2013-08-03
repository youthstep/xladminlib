package com.xunlei.cms.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xunlei.cms.vo.CMSContent;
import com.xunlei.cms.vo.CMSInputType;
import com.xunlei.cms.vo.CMSTemplate;
import com.xunlei.cms.vo.CMSUnit;
import com.xunlei.common.dao.JdbcBaseDao;

@Component
public class CMSDaoImpl extends JdbcBaseDao implements ICMSDao{

	@Override
	public CMSTemplate queryOneTemplate(long id) {
		return queryOne(CMSTemplate.class, "select * from cmstemplate where seqid="+id);
	}

	@Override
	public CMSUnit queryOneUnit(long id) {
		return queryOne(CMSUnit.class, "select * from cmsunit where seqid=" + id);
	}

	@Override
	public CMSInputType queryOneInputType(long id) {
		return queryOne(CMSInputType.class, "select * from cmsinputtype where seqid=" + id);
	}

	@Override
	public List<CMSContent> queryContentsByUnit(long unitid,int contentStatus) {
		return query(CMSContent.class, "select * from cmscontent where unitid=" + unitid + " and status=" + contentStatus + " order by displayorder desc ,inputtime desc");
	}

	@Override
	public List<CMSContent> queryContents(long unitd, int contentStatus, int num) {
		return query(CMSContent.class, "select * from cmscontent where unitid=" + unitd + " and status=" + contentStatus + "  order by displayorder desc ,inputtime desc"+(num>0?" limit 0," + num:""));
	}
	
	@Override
	public List<CMSContent> queryContents(long[] unitds, int contentStatus, int num) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<unitds.length-1;i++){
			sb.append(unitds[i]);
			sb.append(',');
		}
		sb.append(unitds[unitds.length-1]);
		return query(CMSContent.class, "select * from cmscontent where unitid in (" + sb.toString() + ") and status=" + contentStatus + "  order by displayorder desc ,inputtime desc"+(num>0?" limit 0," + num:""));
	}
	
	@Override
	public List<CMSInputType> queryCMSInputTypes(){
		return query(CMSInputType.class, "select * from cmsinputtype order by displayorder asc");
	}
	
	@Override
	public CMSInputType queryCMSInputTypeById(long id){
		return queryOne(CMSInputType.class, "select * from cmsinputtype where seqid="+id);
	}

	@Override
	public List<CMSTemplate> queryCMSTemplate() {
		return query(CMSTemplate.class, "select * from cmstemplate order by displayorder asc");
	}
	
	@Override
	public CMSTemplate queryCMSTemplateById(long id) {
		return queryOne(CMSTemplate.class, "select * from cmstemplate where seqid="+id);
	}
	
	@Override
	public List<CMSUnit> queryCMSUnits(String flatno){
		return query(CMSUnit.class, "select * from cmsunit where flatno='"+flatno+"' order by displayorder asc");
	}
	
	@Override
	public CMSUnit queryCMSUnitById(long id){
		return queryOne(CMSUnit.class, "select * from cmsunit where seqid=" + id);
	}
}
