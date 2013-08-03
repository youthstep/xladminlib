package com.xunlei.common.bo;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunlei.common.util.StringTools;
import com.xunlei.libfun.vo.UserInfo;


/**
 * @author Brice Li(tcwdsg@gmail.com)
 * 
 */
public abstract class AbstractService extends BaseBo{
	protected abstract HttpServletRequest getHttpServletRequest();
	protected abstract HttpServletResponse getHttpServletResponse();
	
	/**
	 * @return 客户端IP
	 */
	protected String getClientAddr() {
		return getHttpServletRequest().getRemoteAddr();
	}

	/**
	 * @param key
	 * @return
	 */
	protected Object getSession(String key) {
		return getHttpServletRequest().getSession().getAttribute(key);
	}

	/**
	 * @param key
	 * @param value
	 */
	protected void setSession(String key, Object value) {
		getHttpServletRequest().getSession().setAttribute(key,value);
	}
	
	protected void removeSession(String key){
		getHttpServletRequest().getSession().removeAttribute(key);
	}
	
	/**
	 * @return
	 */
	protected String getContextPath(){
        return getHttpServletRequest().getContextPath();
    }
	
    /**
     * @param name
     * @param obj
     */
    protected void setRequestAttribute(String name, Object obj){
        getHttpServletRequest().setAttribute(name, obj);
    }

    /**
     * @param name
     * @return
     */
    protected String getParameter(String name){
        return getHttpServletRequest().getParameter(name);
    }
    
    /**
     * 获取request中的参数值，并强制转换为float型
     * @param param
     * @return
     */
    protected float getParamFloat(String param){
        return Float.parseFloat(getParameter(param));
    }

    /**
     * 获取request中的参数值，并强制转换为short型
     * @param param
     * @return
     */
    protected short getParamShort(String param){
        return Short.parseShort(getParameter(param));
    }

    /**
     * 获取request中的参数值，并强制转换为int型
     * @param param
     * @return
     */
    protected int getParamInt(String param){
        return Integer.parseInt(getParameter(param));
    }

    /**
     * 获取request中的参数值，并强制转换为long型
     * @param param
     * @return
     */
    protected long getParamLong(String param){
        return Long.parseLong(getParameter(param));
    }
    
    /**
     * 获得当前请求中的对应的cookie值。不存在返回null
     * @param key
     * @return
     */
    protected String getCookie(String key){
        String rs=null;
        Cookie[] cookies=this.getHttpServletRequest().getCookies();
        if(cookies != null){
            for(Cookie c:cookies){
                if(c.getName().equals(key)){
                    rs=c.getValue();
                }
            }
        }
        return rs;
    }
    
    /**
     * @param name
     * @param value
     * @param maxage
     */
    protected void addCookie(String name, String value, int maxage){
        Cookie cookie=new Cookie(name, value);
        String cookiePath="/";
        if(StringTools.isNotEmpty(this.getContextPath())){
            cookiePath=this.getContextPath();
        }
        cookie.setPath(cookiePath);
        if(maxage > 0){
            cookie.setMaxAge(maxage);
        }
        else{
            cookie.setMaxAge(-1);
        }
        this.getHttpServletResponse().addCookie(cookie);
    }
    
    
    
    
    /******************逻辑相关********/
    /**
     * 返回当前登录帐号
     * @return
     */
    protected String currentUserLogo(){
        UserInfo userinfo=currentUserInfo();
        return (userinfo == null)?null:userinfo.getUserlogno();
    }
    
    /**
     * 返回当前登录用户信息, 没有则返回null
     * @return UserInfo
     */
    protected UserInfo currentUserInfo(){
        return (UserInfo)getSession(UserInfo.NAME);
    }
}
