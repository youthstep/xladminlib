/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.bo;

import java.util.List;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.libfun.vo.LibClassD;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 系统数组元素模块的业务接口
 * 
 * @author jason
 */
public interface ILibClassDBo {

	/**
	 * 根据classno获得所有符合条件的LibClassD结果集。
	 * @param classno 系统数组编号
	 * @return LibClassD对象的List
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassDByClassNo(String)
	 */
	public List<LibClassD> getLibClassDByClassNo(String classno);
	public DataAccessReturn<LibClassD> query(LibClassD data,QueryInfo pinfo);

	public int countLibclassd(LibClassD libclass);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassD(LibClassD)
	 */
	public void insertLibClassD(LibClassD data);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassD(LibClassD)
	 */
	public void updateLibClassD(LibClassD data);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(LibClassD)
	 */
	public void removeLibClassD(LibClassD data);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibClassds(LibClassD,PagedFliper)
	 */
	public DataAccessReturn<LibClassD> queryLibClassds(LibClassD ld, PagedFliper fliper);	
	
	public List<LibClassD> queryLibClassdsByUserlogno(UserInfo userInfo);
}
