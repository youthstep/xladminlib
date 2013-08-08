package com.xunlei.libfun.bo;

import com.xunlei.common.bo.IDataAccessBo;
import com.xunlei.libfun.vo.UserInfo;
import com.xunlei.libfun.vo.Users;

/**
 * 系统用户模块的业务接口
 * 
 * @author Brice Li
 */
public interface IUsersBo extends IDataAccessBo<Users>{
	public UserInfo login(String userlogo,String password,String ip);
	
	public Users queryUsersByUserLogNo(String userlogno);
}
