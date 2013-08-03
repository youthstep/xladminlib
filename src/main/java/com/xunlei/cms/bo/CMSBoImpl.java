package com.xunlei.cms.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xunlei.cms.constant.CMSContentStatus;
import com.xunlei.cms.core.CreateFileConfig;
import com.xunlei.cms.core.CreateFileUtils;
import com.xunlei.cms.dao.CMSDaoImpl;
import com.xunlei.cms.dao.ICMSDao;
import com.xunlei.cms.vo.CMSContent;
import com.xunlei.cms.vo.CMSInputType;
import com.xunlei.cms.vo.CMSTemplate;
import com.xunlei.cms.vo.CMSUnit;
import com.xunlei.common.bo.BaseBo;

@Component
public class CMSBoImpl extends BaseBo implements ICMSBo{
	@Autowired
	public ICMSDao cmsDao;
	
	public CMSBoImpl() {
		super();
		cmsDao = new CMSDaoImpl();
	}

	@Override
	public void processAll() {
		throw new NoSuchMethodError();
	}

	@Override
	public void processByUnits(long[] unitids) throws Exception {
		if(unitids != null){
			for(long unitid : unitids){
				processByUnit(unitid);
			}
		}
	}

	@Override
	public void processByUnit(long unitid) throws Exception {
		CMSUnit unit = cmsDao.queryOneUnit(unitid);
		if(unit!=null){
			CMSTemplate template = cmsDao.queryOneTemplate(unit.getTemplateid());
			CMSInputType inputType = cmsDao.queryOneInputType(unit.getInputtypeid());
			List<CMSContent> contentList = cmsDao.queryContentsByUnit(unit.getSeqid(), CMSContentStatus.NORMAL);
			for(CMSContent content : contentList){
				process(unit,inputType,template,content);
			}
		}else{
			logger.error("unitid:"+unitid+" is not existed");
		}
	}

	@Override
	public void processByContents(long[] contentids) {
		throw new NoSuchMethodError();
	}

	@Override
	public void processByContent(long contentid) {
		throw new NoSuchMethodError();
	}

	@Override
	public void processByPlatforms(int[] platformids) {
		throw new NoSuchMethodError();
	}

	@Override
	public void processByPlatform(int platformid) {
		throw new NoSuchMethodError();
	}
	
	/**
	 * 所有生成的基本方法
	 * @param unit
	 * @param template
	 * @param content
	 * @throws Exception 
	 */
	private void process(CMSUnit unit,CMSInputType inputType,CMSTemplate template,CMSContent content) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("unit", unit);
		map.put("inputType", inputType);
		map.put("content", content);
		JSONObject jsonData = new JSONObject(content.getContent());
		map.put("data", jsonData);
		
		CreateFileConfig config = new CreateFileConfig(
				template.getCharset(),
				template.getCharset(),
				template.getFilepath(),
				unit.getOutputpath());
		CreateFileUtils.generatePage(config, map);
	}
	
	@Override
	public void genAllContentByUnits(long[] unitIds,Map<String,Object> extData) {
		for(long unitId : unitIds)
			genAllContentByUnit(unitId,extData);
	}

	@Override
	public void genAllContentByUnit(long unitId,Map<String,Object> extData) {
		CMSUnit unit = cmsDao.queryOneUnit(unitId);
		if(unit!=null){
			CMSTemplate template = cmsDao.queryOneTemplate(unit.getTemplateid());
			CMSInputType inputType = cmsDao.queryOneInputType(unit.getInputtypeid());
			List<CMSContent> contentList = cmsDao.queryContentsByUnit(unit.getSeqid(), CMSContentStatus.NORMAL);
			Map<String,Object> data = new HashMap<String, Object>();
			JSONObject jsonData;
			for(CMSContent content : contentList){
				try {
					jsonData = new JSONObject(content.getContent());
					data.put("unit", unit);
					data.put("inputType", inputType);
					data.put("content", content);
					data.put("extData", extData);
					data.put("data", jsonData);
					genContent(data,template,
						unit.getOutputpath()+content.getSeqid()+template.getFilepath().substring(template.getFilepath().lastIndexOf("."), template.getFilepath().length()));
				} catch (JSONException e) {
					logger.error(unit.getFlatno()+"-"+unit.getSeqid()+"-"+content.getSeqid(), e);
				} catch (Exception e) {
					this.logger.error(unit.getFlatno()+"-"+unit.getSeqid()+"-"+content.getSeqid(), e);
				}
			}
		}else{
			logger.error("unitid:"+unitId+" is not existed");
		}
	}
	
	private void genContent(Map<String,Object> data,CMSTemplate template,String outputPath) throws Exception{
		CreateFileConfig config = new CreateFileConfig(
				template.getCharset(),
				template.getCharset(),
				template.getFilepath(),
				outputPath
				);
		CreateFileUtils.generatePage(config, data);
	}

	@Override
	public void genListByUnit(long unitId, int pageSize,long templateId,String outputPath,Map<String, Object> extraData) {
		CMSUnit unit = cmsDao.queryOneUnit(unitId);
		if(unit!=null){
			CMSTemplate template = cmsDao.queryOneTemplate(templateId);
			CMSInputType inputType = cmsDao.queryOneInputType(unit.getInputtypeid());
			List<CMSContent> contentList = cmsDao.queryContentsByUnit(unit.getSeqid(), CMSContentStatus.NORMAL);
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("unit", unit);
			data.put("inputType", inputType);
			data.put("extData", extraData);
			
			int pcount = contentList.size()%pageSize>0?contentList.size()/pageSize + 1 : contentList.size()/pageSize;
			pcount=pcount<1?1:pcount;
			data.put("pagesize", pageSize);
			data.put("pagecount", pcount);
			data.put("pagelast", pcount-1);
			int[] parr = new int[pcount];
			for(int t = 0;t < pcount;t++)
				parr[t] = t;
			data.put("pagearr", parr);
			for(int pno=0;pno < pcount;pno++){
				List<CMSContent> pdata = null;
				if(contentList.size()>0){
					pdata = contentList.subList(pno*pageSize, (pno+1)*pageSize>contentList.size()-1?contentList.size()-1:(pno+1)*pageSize);
				}else{
					pdata = contentList;
				}
				data.put("pagedata", pdata);
				data.put("pno", pno);
				try {
					genContent(
							data,template,
							outputPath+pno+template.getFilepath().substring(template.getFilepath().lastIndexOf("."), template.getFilepath().length()));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}else{
			logger.error("unitid:"+unitId+" is not existed");
		}
	}
}
