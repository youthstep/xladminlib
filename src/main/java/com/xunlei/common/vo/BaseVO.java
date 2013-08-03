package com.xunlei.common.vo;

/**
 * @author Brice Li
 * 此类包含框架表基本字段
 */
public class BaseVO {
	/**
	 * 默认自增主键
	 */
	private long seqid;
	
	/**
	 * 创建时间
	 */
	private String inputtime;
	
	/**
	 * 创建人
	 */
	private String inputby;
	
	/**
	 * 修改时间
	 */
	private String edittime;
	
	/**
	 * 修改人
	 */
	private String editby;
	
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
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
}
