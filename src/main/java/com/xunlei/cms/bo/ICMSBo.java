package com.xunlei.cms.bo;

import java.util.Map;


public interface ICMSBo {
	void processAll();
	void processByUnits(long[] unitids) throws Exception;
	void processByUnit(long unitid) throws Exception;
	void processByContents(long[] contentids);
	void processByContent(long contentid);
	void processByPlatforms(int[] platformids);
	void processByPlatform(int platformid);
	
	void genAllContentByUnits(long[] unitIds,Map<String,Object> extraData);
	void genAllContentByUnit(long unidId,Map<String,Object> extraData);
	void genListByUnit(long unitId,int pageSize,long templateId,String outputPath,Map<String,Object> extraData);
}