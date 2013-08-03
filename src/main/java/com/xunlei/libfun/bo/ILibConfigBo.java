/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.bo;

import java.util.List;

import com.xunlei.libfun.vo.LibConfig;

/**
 * 系统配置模块的业务接口
 * 
 * @author Brice Li
 */
public interface ILibConfigBo{
	public List<LibConfig> getAllLibConfig();

	/**
	 * 根据配置编号获得LibConfig对象列表
	 * @param configno 配置编号
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibConfigByConfigNo(String)
	 */
	public void updateLibConfig(LibConfig data);
}
