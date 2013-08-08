
package com.xunlei.libfun.bo;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.constant.LoginStatus;
import com.xunlei.libfun.vo.UserInfo;
import com.xunlei.libfun.vo.Users;

/**
 * @author Brice Li
 *
 */
@Service("usersService")
@RemoteProxy(name="usersService")
public class UsersBoImpl extends DataAccessBo<Users> implements IUsersBo {

	@Override
	public UserInfo login(String userlogo, String password, String ip) {
		Users user =null;
	    user = queryUsersByUserLogNo(userlogo);
	    UserInfo userinfo = new UserInfo(user);
	    if(user == null) {
	        userinfo.setLoginStatus(LoginStatus.USERNAME_ERR);
	    }else if(!user.isInuse()){
	    	userinfo.setLoginStatus(LoginStatus.NOT_INUSE);
	    }else if(password != null && !user.getUserpassword().equals(password)){
	    	userinfo.setLoginStatus(LoginStatus.PASSWORD_ERR);
	    }else{
	    	userinfo.setLoginStatus(LoginStatus.OK);
	    }
	    return userinfo;
	}
	
	public Users queryUsersByUserLogNo(String userlogno) {
		return utilDao.queryOne(Users.class, "select * from users where userlogno='" + userlogno + "'");
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Users> query(Users data, QueryInfo pinfo) {
//		DataAccessReturn<Users> rtn = super.query(data, pinfo);
//		if(rtn.getDatas() != null && rtn.getDatas().size() > 0){
//			for(Users u : rtn.getDatas()){
//				//为了不让前端获取用户密码的MD值
//				u.setUserpassword("null");
//			}
//		}
//		return rtn;
		return super.query(data, pinfo);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Users> insert(Users data) {
		return super.insert(data);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Users> update(Users data) {
//		//不允许直接修改数据
//		Users u = utilDao.findObject(data);
//		data.setUserpassword(u.getUserpassword());
		return super.update(data);
	}

	@Override
	@RemoteMethod
	public DataAccessReturn<Users> deleteSome(Users[] objs) {
		return super.deleteSome(objs);
	}
}
