package com.xunlei.libfun.vo;

import com.xunlei.common.util.Extendable;
import com.xunlei.common.util.StringTools;



/**
 * 角色的VO类
 */

public class Role  implements java.io.Serializable {

    private long seqid =0L;
    private String no;
    private String name;
    private int type;
    private String remark ="";
    
    // Constructors
    
    /** default constructor */
    public Role() {
    }

	public long getSeqid() {
		return seqid;
	}

	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}