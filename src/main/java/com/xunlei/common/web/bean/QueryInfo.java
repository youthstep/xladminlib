package com.xunlei.common.web.bean;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class QueryInfo {
	/**
     *默认每页显示行数
     */
    public static int DEFAULTPAGESIZE = 50;
    @RemoteProperty
    protected int pageNo = 1;
    protected int recordCount;//记录总数
    protected int pageSize = DEFAULTPAGESIZE;
    protected String sortColumn ="";
    protected String sqlCondition;
    
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[pageNo:"+pageNo+",pageSize:"+pageSize+",where "+sqlCondition+" order by " + sortColumn;
	}
	
	public String getSqlCondition() {
		return sqlCondition;
	}
	public void setSqlCondition(String sqlCondition) {
		this.sqlCondition = sqlCondition;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
}
