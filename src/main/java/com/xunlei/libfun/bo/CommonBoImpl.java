package com.xunlei.libfun.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.XLService;
import com.xunlei.common.util.Constants;
import com.xunlei.common.util.VerifycodeUtil;
import com.xunlei.common.web.bean.DataReturn;
import com.xunlei.libfun.constant.LoginStatus;
import com.xunlei.libfun.constant.PrivilegeTypes;
import com.xunlei.libfun.vo.LibConfig;
import com.xunlei.libfun.vo.Menus;
import com.xunlei.libfun.vo.Privilege;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 
 * @author lixin
 * 常用的一些业务，如配置获取，登录，用户菜单等
 * @since 2011-10-27 上午11:13:33
 */
@Service("commonService")
@RemoteProxy(name="commonService")
public class CommonBoImpl  extends XLService implements ICommonBo{
	@Autowired
	public ILibConfigBo ligConfigBo;
	@Autowired
	public IUsersBo usersBo;
	@Autowired
	public IMenusBo menusBo;
	@Autowired
	public IAuthBo authBo;
	
	/**
	 * 获得系统配置
	 * @return
	 */
	@RemoteMethod
	public DataReturn getConfig(){
		DataReturn rtn = new DataReturn();
		List<LibConfig> configList = ligConfigBo.getAllLibConfig();
		Map<String,String> configMap = new HashMap<String, String>();
		for(LibConfig lc : configList){
			configMap.put(lc.getConfigno(), lc.getConfigvalue());
		}
		rtn.setData(configMap);
		return rtn;
	}

	/**
	 * 登录获取相关的登录信息
	 * @param username
	 * @param password
	 * @param verifyCode
	 * @param verifyCodeCookie
	 * @return
	 */
	@RemoteMethod
	public UserInfo login(String username,String password,String verifyCode){
		UserInfo userInfo = null;
		if("true".equals(LibConfig.getValue("VerifyCode")) && !VerifycodeUtil.isVerify(this.getHttpServletRequest(),verifyCode)){
            userInfo = new UserInfo(null);
            userInfo.setLoginStatus(LoginStatus.VERIFY_CODE_ERR);
        }else{
        	userInfo = usersBo.login(username, password, getClientAddr());
        	if(userInfo.getLoginStatus() == LoginStatus.OK){
        		setSession(UserInfo.NAME, userInfo);
                setSession(Constants.LOGINCLIENTIP,this.getHttpServletRequest().getRemoteAddr());
                //init privilege
                List<Privilege> pList = authBo.getUserPrivilegeByType(username, PrivilegeTypes.SERVICE_METHOD);
                if(pList != null){
                	for(Privilege p : pList){
                		userInfo.getServicePrivilegeMap().put(p.getName() + "-" + p.getValue(), p);
                	}
                }
                
                pList = authBo.getUserPrivilegeByType(username, PrivilegeTypes.MENU);
                if(pList != null){
                	for(Privilege p : pList){
                		userInfo.getMenunoList().add(p.getValue());
                	}
                }
        	}
        }
		return userInfo;
	}
	
	/**
	 * 检查用户是否已经登录
	 * @return
	 */
	@RemoteMethod
	public UserInfo hasLogin(){
		return currentUserInfo();
	}
	
	/**
	 * 用户注销 
	 */
	@RemoteMethod
	public void logout(){
		removeSession(UserInfo.NAME);
	}
	
	/**
	 * 获得当前用户菜单
	 * @return
	 */
	@RemoteMethod
	public List<Menus> getMenus(){
		return menusBo.getAllMenusByUserInfo(currentUserInfo());
	}
}
