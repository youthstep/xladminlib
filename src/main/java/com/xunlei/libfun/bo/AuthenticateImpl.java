package com.xunlei.libfun.bo;



import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.directwebremoting.annotations.RemoteMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xunlei.common.bo.BaseBo;
import com.xunlei.common.bo.XLService;
import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.StringTools;
import com.xunlei.libfun.vo.UserInfo;
@Aspect
@Component
public class AuthenticateImpl extends XLService implements IAuthenticate{
	protected static final Log logger = LogFactory.getLog("auth");
	@Autowired
	public IAuthBo authBo;
	
	//不能用or来连接各包的方法 by super  2012-11-28 21:19:46
	@Before("@org.directwebremoting.annotations.RemoteMethod * *(..)")
	public void doAuth(JoinPoint joinPoint)  {
		MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature(); 
		Method method =  joinPointObject.getMethod();
		//只有RemotingInclude的才控制
//		if(meth!=null){
			//进入权限控制
//			String ip = getClientAddr();
//			UserInfo userinfo= currentUserInfo();
//			String className = joinPoint.getClass().getName();
//			String funName = joinPointObject.getMethod().getName();
//			if(userinfo==null||StringTools.isEmpty(userinfo.getUserlogno())){
//				logger.warn("nulluser[" + ip + "] try to access " + className + ":" + funName);
//				throw new XLRuntimeException("用户未登录");
//			}else if(userinfo.isSuperman()){
//				logger.info("admin:"+userinfo.getUserlogno()+"[" + ip + "] access " + className + ":" + funName);
//			}else{
//				if(authBo.hasPermission(userinfo.getUserlogno(), className, funName)){
//					logger.info("u:"+userinfo.getUserlogno()+"[" + ip + "] access " + className + ":" + funName);
//				}else{
//					logger.warn("u:"+userinfo.getUserlogno()+"[" + ip + "] deny access " + className + ":" + funName);
//					throw new XLRuntimeException("用户无权限");
//				}
//			}
//		}else{//by super 2012-11-29 10:44:17 对不含RemotingInclude标签的方法进行登录判断
//			boolean isNeed = true;
//			for (String methodName : arr) {
//				if(joinPointObject.getMethod().getName().equals(methodName)){
//					isNeed = false;
//					break;
//				}
//			}
//			if(isNeed){
//				//进入权限控制
//				String ip = getClientAddr();
//				UserInfo userinfo= currentUserInfo();
//				String className = joinPoint.getClass().getName();
//				String funName = joinPointObject.getMethod().getName();
//				if(userinfo==null||StringTools.isEmpty(userinfo.getUserlogno())){
//					logger.warn("nulluser[" + ip + "] try to access " + className + ":" + funName);
//					throw new XLRuntimeException("用户未登录");
//				}
//			}
//		}
	}
}
