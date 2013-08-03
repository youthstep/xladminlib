/**
 * 
 */
package com.xunlei.libfun.bo;

import org.aspectj.lang.JoinPoint;

/**
 * @author liheng
 * @since 2011-11-10 下午3:29:45
 */
public interface IAuthenticate {
	public void doAuth(JoinPoint joinPoint);
}
