package com.xunlei.common.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserCookiePluginable extends Pluginable {
	

	public boolean filterCookie();
	public void setRequest(HttpServletRequest request);
	public void setResponse(HttpServletResponse response);
}
