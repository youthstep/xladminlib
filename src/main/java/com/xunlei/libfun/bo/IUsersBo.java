package com.xunlei.libfun.bo;

import com.xunlei.libfun.vo.UserInfo;
import com.xunlei.libfun.vo.Users;

/**
 * 系统用户模块的业务接口
 * 
 * @author Brice Li
 */
public interface IUsersBo{
	public UserInfo login(String userlogo,String password,String ip);
	
	public Users queryUsersByUserLogNo(String userlogno);
	
	public void updatePasswordByUsername(String username, String password);
}
