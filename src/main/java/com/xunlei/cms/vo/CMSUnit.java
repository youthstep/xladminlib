package com.xunlei.cms.vo;

public class CMSUnit {
	private long seqid;
	private String edittime;
	private String editby;
	private String inputtime;
	private String inputby;
	private int displayorder;
	private String remark;
	private String name;
	private int typeid;
	private long parentid;
	private long inputtypeid;
	private long outputtypeid;
	private long templateid;
	private String outputpath;
	private String outputcharset;
	private int outputtype;
	public String flatno;
	/**
	 * @see com.xunlei.cms.constant.CMSUnitStatus
	 */
	private int status;
	
	public long getTemplateid() {
		return templateid;
	}
	public void setTemplateid(long templateid) {
		this.templateid = templateid;
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
	public int getDisplayorder() {
		return displayorder;
	}
	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public long getParentid() {
		return parentid;
	}
	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	public long getInputtypeid() {
		return inputtypeid;
	}
	public void setInputtypeid(long inputtypeid) {
		this.inputtypeid = inputtypeid;
	}
	public long getOutputtypeid() {
		return outputtypeid;
	}
	public void setOutputtypeid(long outputtypeid) {
		this.outputtypeid = outputtypeid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOutputpath() {
		return outputpath;
	}
	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}
	public String getFlatno() {
		return flatno;
	}
	public void setFlatno(String flatno) {
		this.flatno = flatno;
	}
	public String getOutputcharset() {
		return outputcharset;
	}
	public void setOutputcharset(String outputcharset) {
		this.outputcharset = outputcharset;
	}
	public int getOutputtype() {
		return outputtype;
	}
	public void setOutputtype(int outputtype) {
		this.outputtype = outputtype;
	}
}
