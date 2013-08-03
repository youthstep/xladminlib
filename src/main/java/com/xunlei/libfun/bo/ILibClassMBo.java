/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.bo;

import java.util.List;

import com.xunlei.libfun.dao.ILibClassMDao;
import com.xunlei.libfun.vo.LibClassM;

/**
 * 系统数组模块的业务接口
 * 
 * @author jason
 */
public interface ILibClassMBo {

	public void setLibClassMDao(ILibClassMDao libClassMDao);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibClassM()
	 */
	public List<LibClassM> getAllLibClassM();

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassMById(Long)
	 */
	public LibClassM getLibClassMById(Long id);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassM(LibClassM)
	 */
	public void insertLibClassM(LibClassM data);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassM(LibClassM)
	 */
	public void updateLibClassM(LibClassM data);

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(LibClassM)
	 */
	public void removeLibClassM(LibClassM data);

	/**
	 * 获得符合查询条件的LibClassM对象个数
	 * @param libclass 查询的条件
	 * @return 结果个数
	 */
	public int countLibclassm(LibClassM libclass);
}
