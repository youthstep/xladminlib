package com.xunlei.common.bo;

import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;

public interface IDataAccessBo<T> {
	public DataAccessReturn<T> query(T data,QueryInfo pinfo);
	public DataAccessReturn<T> insert(T data);
	public DataAccessReturn<T> update(T data);
	public DataAccessReturn<T> deleteSome(T[] objs);
}
