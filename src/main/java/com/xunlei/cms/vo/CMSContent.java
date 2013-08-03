package com.xunlei.cms.vo;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.xunlei.common.util.Extendable;
import com.xunlei.common.util.StringTools;


public class CMSContent {
	private long seqid;
	
	private String edittime;
	private String editby;
	private String inputtime;
	private String inputby;
	private long unitid;
	public String title;
	private String content;
	private int displayorder;
	@Extendable
	private String ext1;
	/**
	 * @see com.xunlei.cms.constant.CMSContentStatus
	 */
	private int status;
	private String remark;
	
	public String getInputtime1(){
		if(StringTools.isNotEmpty(this.inputtime)&&this.inputtime.length()==19){
			return this.inputtime.substring(5, 10);
		}else{
			return "";
		}
	}
	public String getInputtime2(){
		if(StringTools.isNotEmpty(this.inputtime)&&this.inputtime.length()==19){
			return this.inputtime.substring(0, 10);
		}else{
			return "";
		}
	}
	public JSONObject getContentJson(){
		JSONObject obj;
		try {
			obj = new JSONObject(getContent());
		} catch (JSONException e) {
			e.printStackTrace();
			obj = null;
		}
		return obj;
	}
	
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public String getEditby() {
		return editby;
	}
	public void setEditby(String editby) {
		this.editby = editby;
	}
	public String getInputtime() {
		return inputtime;
	}
	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}
	public String getInputby() {
		return inputby;
	}
	public void setInputby(String inputby) {
		this.inputby = inputby;
	}
	public long getUnitid() {
		return unitid;
	}
	public void setUnitid(long unitid) {
		this.unitid = unitid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getDisplayorder() {
		return displayorder;
	}
	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
}
