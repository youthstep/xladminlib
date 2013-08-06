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

	public long getSeqid() {
		return seqid;
	}

	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
}
