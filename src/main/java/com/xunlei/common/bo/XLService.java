package com.xunlei.common.bo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContextFactory;

public abstract class XLService extends AbstractService{
	
	@Override
	protected HttpServletRequest getHttpServletRequest() {
		//不可以缓存WebContextFactory.get() 会导致 getSession出问题，和线程相关
		return WebContextFactory.get().getHttpServletRequest();
	}

	@Override
	protected HttpServletResponse getHttpServletResponse() {
		return WebContextFactory.get().getHttpServletResponse();
	}
}