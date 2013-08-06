package com.xunlei.common.bo;

import org.springframework.beans.factory.annotation.Autowired;

import com.xunlei.common.dao.IUtilDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;

/**
 * 
 * @author liheng
 * 通用的数据增删改查Bo
 * @param <T>
 */
public class DataAccessBo<T> extends XLService implements IDataAccessBo<T>{
	@Autowired
	public IUtilDao utilDao;
	
	@Override
	public DataAccessReturn<T> query(T data,QueryInfo pinfo){
		return utilDao.findPagedObjects(data, pinfo.getSqlCondition(), new PagedFliper(pinfo));
	}
	
	@Override
	public DataAccessReturn<T> insert(T data){
		DataAccessReturn<T> rtn = new DataAccessReturn<T>();
		utilDao.insertObject(data);
		return rtn;
	}
	
	@Override
	public DataAccessReturn<T> update(T data){
		DataAccessReturn<T> rtn = new DataAccessReturn<T>();
		utilDao.updateObject(data);
		return rtn;
	}
	
	@Override
	public DataAccessReturn<T> deleteSome(T[] objs){
		DataAccessReturn<T> rtn = new DataAccessReturn<T>();
		for (T t : objs) {
			utilDao.deleteObject(t);
		}
		return rtn;
	}
	
	/**
	 * 构建一个只包含返回码和消息的DataAccessReturn,尤其适合构建错误信息返回
	 * @param code DataAccessReturn.ERROR..
	 * @param msg
	 * @return
	 */
	protected DataAccessReturn<T> genDataAccessReturn(int code, String msg){
		DataAccessReturn<T> rtn = new DataAccessReturn<T>();
		rtn.setCode(code);
		rtn.setMsg(msg);
		return rtn;
	}
}
