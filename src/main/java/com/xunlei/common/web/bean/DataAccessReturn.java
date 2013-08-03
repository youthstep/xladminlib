package com.xunlei.common.web.bean;

import java.util.Collection;
import java.util.Collections;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class DataAccessReturn<T> extends AbstractReturn{
	public static final int DB_ERROR = 3;//数据库错误
	public static final int OK = 0;//成功
	public static final int ERROR = 1;//未知错误
	
	@SuppressWarnings("unchecked")
    /**
    * 用EMPTY表示空的数据列表
    */
    public static final DataAccessReturn EMPTY = new DataAccessReturn(0, Collections.EMPTY_LIST);

	protected Collection<T> datas;//列表数据
	protected Object data;
	protected int rowcount = -1;//数据总个数
	
	/**
	 * 默认code 为OK 
	 */
	public DataAccessReturn(){
		setCode(OK);
	}
	
	public DataAccessReturn(int rowcount,Collection<T> datas){
		this.rowcount = rowcount;
		this.datas = datas;
	}
	
	@Override
	public String toString(){
		return "{code:"+this.getCode()+",rowcount:"+this.getRowcount()+",datas:"+this.getDatas().size()+",msg:"+this.getMsg()+"}";
	}
	
	/**
     *@return 数据列表的数据
     */
    public Collection<T> getDatas() {
        return datas;
    }
    
    /**
     *设置数据列表的数据
     */
    public void setDatas(Collection<T> datas) {
        this.datas = datas;
    }
    
    /**
     *@return 数据列表的数据总个数
     */
    public int getRowcount() {
        return rowcount;
    } 

    /**
     *设置数据列表的数据总个数
     */
    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
