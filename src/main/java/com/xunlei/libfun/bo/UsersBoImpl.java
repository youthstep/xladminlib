/*
 * Created on 2006-03-10
 */
package com.xunlei.libfun.bo;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.bo.XLService;
import com.xunlei.common.util.DatetimeUtil;
import com.xunlei.common.util.MD5Hash;
import com.xunlei.common.util.StringTools;
import com.xunlei.common.util.VerifycodeUtil;
import com.xunlei.libfun.constant.LoginStatus;
import com.xunlei.libfun.vo.UserInfo;
import com.xunlei.libfun.vo.Users;

/**
 * 系统用户模块的业务逻辑实现层
 * 
 * @author jason
 */
@Service("usersService")
@RemoteProxy
public class UsersBoImpl extends DataAccessBo<Users> implements IUsersBo {

	@Override
	public UserInfo login(String userlogo, String password, String ip) {
		Users user =null;
	    user = getUsersByUserLogNo(userlogo);
	    UserInfo userinfo = new UserInfo(user);
	    if(user == null) {
	        userinfo.setLoginStatus(LoginStatus.USERNAME_ERR);
	    }else if(!user.isInuse()){
	    	userinfo.setLoginStatus(LoginStatus.NOT_INUSE);
	    }else if(StringTools.isNotEmpty(user.getStartvaliddate())&& user.getStartvaliddate().compareTo(DatetimeUtil.today())>0){
	    	userinfo.setLoginStatus(LoginStatus.EXPIRE);
	    }else if(StringTools.isNotEmpty(user.getEndvaliddate())&& user.getEndvaliddate().compareTo(DatetimeUtil.today())<0){
	    	userinfo.setLoginStatus(LoginStatus.EXPIRE);
	    }else if(password != null && !user.getUserpassword().equals(password)){
	    	userinfo.setLoginStatus(LoginStatus.PASSWORD_ERR);
	    }else{
	    	userinfo.setLoginStatus(LoginStatus.OK);
	    }
	    return userinfo;
	}
	
	public Users getUsersByUserLogNo(String userlogno) {
		return utilDao.queryOne(Users.class, "select * from users where userlogno='" + userlogno + "'");
	}
//    @Autowired
//	private IUsersDao usersDao;
//    @Autowired
//    public IUtilDao utilDao;
//    @Autowired
//    public IRolesBo rolesBo;
//    @Autowired
//    public IUserToRoleBo userToRoleBo;
//    @Autowired 
//    public IUsertobiznoBo usertobiznoBo;
//    @Autowired
//    public IFunctionsBo functionsBo;
//	
//	@RemotingInclude
//    public DataAccessReturn<Users> query(Users data,QueryInfo pinfo){
//		DataAccessReturn<Users> rtn = new DataAccessReturn<Users>();
//		rtn.setDatas(utilDao.findPagedObjects(data, pinfo.getSqlCondition(), new PagedFliper(pinfo)).getDatas());
//		rtn.setRowcount(rtn.getDatas().size());
//		return rtn;
//	}
//
//	@RemotingInclude
//	public DataAccessReturn<Users> insert(Users data){
//		data.setDecodePassword(data.getUserpassword());
//		DataAccessReturn<Users> rtn = new DataAccessReturn<Users>();
//		if (getUsersByUserLogNo(data.getUserlogno()) != null) {
//			throw new XLRuntimeException("已经存在相同帐号的用户");
//		}
//		if (!StringTools.isEmpty(data.getCopartnerno()) && !StringTools.isEmpty(data.getUpuserlogno())&& getUsersByUserLogNo(data.getUpuserlogno())!=null && !data.getCopartnerno().equals(getUsersByUserLogNo(data.getUpuserlogno()).getCopartnerno()))
//		{
//			throw new XLRuntimeException("子帐号合作商和上级主帐号的合作商不符");
//		}
//        //用户是否符合要求
//        for(String s:Constants.EXCLUDE_USERS){
//            if(s.equalsIgnoreCase(data.getUserlogno())){
//                throw new XLRuntimeException("不允许新增"+data.getUserlogno()+"帐号");
//            }
//        }
//		getUsersDao().insertUsers(data);
//		return rtn;
//	}
//	
//	@RemotingInclude
//	public DataAccessReturn<Users> update(Users data){
//		DataAccessReturn<Users> rtn = new DataAccessReturn<Users>();
//		Users otherdata = getUsersByUserLogNo(data.getUserlogno());
//		if (otherdata != null && otherdata.getSeqid() != data.getSeqid()) {
//			throw new XLRuntimeException("已经存在相同帐号的用户");
//		}
//		getUsersDao().updateUsers(data);
//		return rtn;
//	}
//	
//	@RemotingInclude
//	public DataAccessReturn<Users> deleteSome(Users[] objs){
//		DataAccessReturn<Users> rtn = new DataAccessReturn<Users>();
//		for (Users data : objs) {
//			if (userToRoleBo.getUserToRoleByUserLogNo(data.getUserlogno())> 0) {
//				throw new XLRuntimeException("用户("+data.getUserlogno()+")已被用户角色引用，不能删除");
//			}
//	       //查询是否被用户邮件组引用
//	       Usertomailclass utmc=new Usertomailclass();
//	       utmc.setUserlogno(data.getUserlogno());
//			Users queryuser = new Users();
//			queryuser.setUpuserlogno(data.getUserlogno());
//			DataAccessReturn<Users> sheet = getUsersDao().queryUsers(queryuser, null);
//			if (sheet.getRowcount()> 0) {
//				throw new XLRuntimeException("请先删除用户("+data.getUserlogno()+")的子帐号");
//			}
//	       if(StringTools.isNotEmpty(data.getUpuserlogno())){//删除子帐号的时候要删除此帐号的所有权限
//	           //删除子帐号的所有数据权限
//	           Usertobizno forDelbizno=new Usertobizno();
//	           forDelbizno.setUserlogno(data.getUserlogno());
//	           usertobiznoBo.deleteUsertobizno(forDelbizno);
//	       }
//			getUsersDao().removeUsers(data);
//		}
//		return rtn;
//	}
//    
//    @Override
//	public List<BaseCopartners> queryCopartners() {
//		Map<String, String> map = getCopartnerNoAndName();
//		List<BaseCopartners> dataList = new ArrayList<BaseCopartners>();
//        for(Map.Entry<String, String> enty : map.entrySet()) {
//        	BaseCopartners data =new BaseCopartners();
//        	data.setCopartnerno(enty.getKey());
//        	data.setCopartnername(enty.getValue());
//        }
//        return dataList;
//    }
//	/**
//     * 重置某用户密码
//     * @param seqid
//     * @param password
//     */
//    @Override
//    @RemotingInclude
//    public void resetPassword(long seqid,String password){
//        Users user = getUsersById(seqid);
//        user.setDecodePassword(password);
//        user.setEditby(user.getUserlogno());
//        user.setEdittime(DatetimeUtil.now());
//        update(user);
//    }
//    
//    @Override
//    public List<Users> queryUpusers() {
//    	 Map<String, String> map = getUpuserlognolist();
// 		 List<Users> dataList = new ArrayList<Users>();
//         for(Map.Entry<String, String> enty : map.entrySet()) {
//        	Users data =new Users();
//         	data.setUpuserlogno(enty.getKey());
//         	data.setTruename(enty.getValue());
//         }
//         return dataList;
//     }
//     
//    @Override
//    @AuthOper(AuthConstants.OPERATE_EDIT)
//     public List<Roles> queryRolesRelateUser(Users user){
//         return rolesBo.getAllRolesInUsers(user);
//     }
//     
//    @Override
//    @AuthOper(AuthConstants.OPERATE_RUN)
//     public List<Roles> queryRolesNotRelateUser(Users user) {
//         return rolesBo.getAllRolesNotInUsers(user);
//     }
//     
//    @Override
//    @AuthOper(AuthConstants.OPERATE_ADD)
//     public DataAccessReturn<Roles> addRolesToUser(long[] sids,Users user){
//    	 DataAccessReturn<Roles> rtn = new DataAccessReturn<Roles>();
//    	 userToRoleBo.addRolestoUserToRoles(user, sids);
//         return rtn;
//     }
//     
//    @Override
//    @AuthOper(AuthConstants.OPERATE_DEL)
//     public DataAccessReturn<Roles> removeRolesToUser(long[] sids,Users user){
//    	 DataAccessReturn<Roles> rtn = new DataAccessReturn<Roles>();
//    	 userToRoleBo.removeRolesfromUserToRoles(user, sids);
//         return rtn;
//     }
//     
//    @Override
//    @AuthOper(AuthConstants.OPERATE_EDIT)
//     public void updateSelfInfo(String oldPwd,String newPwd,String tel,String qq,String mail){
// 		updatePassword(currentUserLogo(), oldPwd, newPwd, tel, qq, mail);
// 	}
// 	
//    @Override
//    @AuthOper(AuthConstants.OPERATE_RUN)
// 	public Users querySelfInfo(){
// 		return getUsersByUserLogNo(currentUserLogo());
// 	}
//    
//    /**以下为非远程方法********************************/
//    /**
//	 * 根据seqid获取相应的系统用户记录。
//	 */
//    
//	@Deprecated
//    public Users getUsersById(Long id) {
//        Users u=new Users();
//        u.setSeqid(id);
//		return  getUsersDao().getAUsers(u);
//
//	}
//
//	/**
//	 * 根据系统用户帐号获取相应的系统用户记录，包括其合作商名称的信息。
//	 */
//   

//
//	/**
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getCopartnerNoAndName()
//	 */
//   
//	public Map<String, String> getCopartnerNoAndName() {
//		return getUsersDao().getCopartnerNoAndName();
//	}
//
//	/**
//	 * @return 返回在用的业务用户（只包含属性CopartnerNo, CopartnerName）的列表。
//	 * 
//	 */
//   
//	public List<BaseCopartners> getBaseCopartnerNoAndName() {
//		List<BaseCopartners> bcl = new ArrayList<BaseCopartners>(0);
//		Map<String, String> m = getUsersDao().getCopartnerNoAndName();
//		if (m != null) {
//			Set<String> s = m.keySet();
//			Iterator<String> li = s.iterator();
//			while(li.hasNext()) {
//				String no = li.next();
//				BaseCopartners  bc = new BaseCopartners();
//				bc.setCopartnerno(no);
//				bc.setCopartnername(m.get(no));
//				bcl.add(bc);
//			}
//		}
//		return bcl;
//	}
//   
//	public UserRights getUserRightsByFuncNo(String userlogno, String funcno) {
//		// 用户信息
//		final Users user = getUsersByUserLogNo(userlogno);
//		// 功能模块
//		final Functions funs = (Functions) functionsBo.getFunctionsByFuncNo(funcno).get(0);
//		// 用户信息
//		final UserRights data = new UserRights(user, funs);
//		// 管理员
//		if (user.getSuperman() == 1)
//			return data;
//		// .....................
//		return data;
//	}
//
//	@Deprecated
//	
//	public int getUsersViewCount(Users u) {
//		return getUsersDao().getUsersViewCount(u);
//	}
//
//
//	/**
//	 * 是否对用户进行数据控制
//	 * @param userlogno 用户帐号
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getIfMyDataOnly(String)
//	 */
//	
//	public boolean getIfMyDataOnly(String userlogno) {
//		return getUsersDao().getIfMyDataOnly(userlogno);
//	}
//
//	/**
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getAllUserLogo()
//	 */
//	@RemotingInclude
//	@Override
//	public List<String> getAllUserLogo() {
//		return getUsersDao().getAllUserLogo();
//	}
//
//	/**
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getCountUsers(Users)
//	 */
//	public int getCountUsers(Users data) {
//		return getUsersDao().getCountUsers(data);
//	}
//	
//	/**
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#isSuperMan(Users)
//	 */
//	public boolean isSuperMan(Users u) {
//		return getUsersDao().isSuperMan(u);
//	}
//	
//	public Users findUsers(Users data){
//		return this.getUsersDao().findAUsers(data);
//	}
//
//
//    public DataAccessReturn<Users> querySubUsers(String fatheruser, PagedFliper fliper) {
//        Users u=new Users();
//        u.setUpuserlogno(fatheruser);
//        return this.getUsersDao().queryUsers(u, fliper);
//    }
//    
//    /**
//	 * 获得所有的上级主帐号
//	 */
//	public Map<String, String> getUpuserlognolist(){
//		return this.getUsersDao().getUpuserlognolist();
//	}
//    /**
//	 * 获得所有的上级主帐号(users列表)
//	 */
//	public List<Users> getUpuserslist(){
//		List<Users> bcl = new ArrayList<Users>(0);
//		Map<String, String> m = getUsersDao().getUpuserlognolist();
//		if (m != null) {
//			Set<String> s = m.keySet();
//			Iterator<String> li = s.iterator();
//			while(li.hasNext()) {
//				String no = li.next();
//				Users  bc = new Users();
//				bc.setUserlogno(no);
//				bc.setTruename(m.get(no).replace(no+" ", ""));
//				bcl.add(bc);
//			}
//		}
//		return bcl;
//	}
//
//    public Users findAUsers(Users users){
//    	return findUsers(users);
//    }
//
//    /**
//     * 判断某帐号是否是某帐号的子帐号
//     * @param subaccount 子帐号
//     * @param fatheraccount 主帐号
//     * @return 当帐号是某帐号的子帐号时返回true，否则返回false
//     */
//    public boolean isSubAccountof(String subaccount,String fatheraccount){
//        Users user=new Users();
//        user.setUserlogno(subaccount);
//        user.setUpuserlogno(fatheraccount);
//        user=findAUsers(user);
//        if(user!=null){
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 获得前台用户的用户名和真实名类表，每项使用|进行分割
//     * @return
//     */
//    public String[] getFrontUsersLognoAndTrueName(){
//        return this.getUsersDao().getFrontUsersLognoAndTrueName();
//    }
//
//        /**
//     * 查询当前用户登录相关的信息
//     * @param onlineUserInfoShow
//     * @param fliper
//     * @return
//     */
//    public DataAccessReturn<OnlineUserInfoShow> queryAllUsersWithOnlineInfo(OnlineUserInfoShow onlineUserInfoShow,PagedFliper fliper){
//        return this.getUsersDao().queryAllUsersWithOnlineInfo(onlineUserInfoShow, fliper);
//    }
//
//    /**
//     * 更新用户密码
//     * @param userlogno
//     * @param oldPassword
//     * @param newPassword
//     * @param tel
//     * @param qq
//     * @param mail
//     */
//    public void updatePassword(String userlogno,String oldPassword,String newPassword,String tel,String qq,String mail){
//        final Users user = getUsersByUserLogNo(userlogno);
//        if(/*!user.getDecodePassword().equals(oldPassword)*/
//                !user.getUserpassword().equals(MD5Hash.encryptPwd(oldPassword))) {
//            throw new XLRuntimeException("输入的旧密码不正确.");
//        }
//        user.setDecodePassword(newPassword);
//        user.setEditby(user.getUserlogno());
//        user.setEdittime(DatetimeUtil.now());
//        user.setTel(tel);
//        user.setQq(qq);
//        user.setEmail(mail);
//        update(user);
//    }
//
//
//    /**
//     * 修改用户信息逻辑
//     * @param user
//     */
//    public void editUsers(Users data){
//        if(StringTools.isEmpty(data.getCopartnerno())) data.setCopartnerno("");
//        Users oldusers=getUsersByUserLogNo(data.getUserlogno());
//        data.setUserpassword(oldusers.getUserpassword());
//        data.setUserlogintype(oldusers.getUserlogintype());
//        data.setEdittime(DatetimeUtil.now());
//        if(data.isSubaccount()){//子帐户只能是前台登录
//            data.setUserlogintype("B");
//        }
//        update(data);
//    }
//
//
//    @Override
//    public UserInfo Login(String userlogo,String password,String ip) {
//        Users user =null;
//        try{
//            user = getUsersByUserLogNo(userlogo);
//        }catch(Exception ex){
//            if(ex.getClass()==XLRuntimeException.class){
//               throw new XLRuntimeException("-1|"+ex.getMessage());
//             }
//             else if(ex.getClass()==CommunicationsException.class){
//                throw new XLRuntimeException("-4|系统与数据库通信失败，请稍后重试。");
//             }
//             else{
//                throw new XLRuntimeException("-4|查询数据库时发生错误，请稍后重试。");
//            }
//            
//        }
//        if(user == null) {
//            throw new XLRuntimeException("-1|帐号(" + userlogo + ") 不存在.");
//        }
//        //主帐号和子帐号都不允许登录后台
//        if(StringTools.isNotEmpty(user.getUpuserlogno()) ||!user.getUserlogintype().equals("A")){
//            throw new XLRuntimeException("-1|此帐号不允许登录后台.");
//        }
//        if(StringTools.isNotEmpty(user.getBindip())){
//            String[] strs = user.getBindip().split(",");
//            boolean  illed = true;
//            for(String s : strs){
//                if(ip.equals(s.trim())){
//                    illed = false;break;
//                }
//            }
//            if(illed){
//                 throw new XLRuntimeException("-1|非法客户端登录.");
//            }
//        }
//        //密码为空时，则不需要校验密码
//        if(password != null && !user.getUserpassword().equals(MD5Hash.encryptPwd(password))){
//            throw new XLRuntimeException("-2|密码错误.");
//        }        
//        if(user.getInuse() != 1){
//            throw new XLRuntimeException("-1|帐号(" + userlogo + ")未生效.");
//        }
//        if(StringTools.isNotEmpty(user.getStartvaliddate())&& user.getStartvaliddate().compareTo(DatetimeUtil.today())>0){
//            throw new XLRuntimeException("-1|帐号(" + userlogo + ")未生效.");
//        }
//        if(StringTools.isNotEmpty(user.getEndvaliddate())&& user.getEndvaliddate().compareTo(DatetimeUtil.today())<0){
//             throw new XLRuntimeException("-1|帐号(" + userlogo + ")已失效.");
//        }
//        List<String> sysroles = new ArrayList<String>(3);
//        List<String> recroles = new ArrayList<String>(3);
//        for(UserToRole utr : user.getRoles()) {
//            if(Constants.ROLETYPE_SYS.equals(utr.getRoletype())) {
//                sysroles.add(utr.getRoleno());
//            } else if (Constants.ROLETYPE_REC.equals(utr.getRoletype())) {
//                recroles.add(utr.getRoleno());
//            }
//        }
//        UserInfo userinfo = new UserInfo(userlogo, user.getTruename()
//        , sysroles.toArray(new String[sysroles.size()])
//        , recroles.toArray(new String[recroles.size()])
//        , (user.getSuperman() > 0)
//        , getIfMyDataOnly(userlogo),user.getWorkingplatform(),user.isBoolsinglelogin());
//        return userinfo;
//    }
//    
//	public IUsersDao getUsersDao() {
//		return this.usersDao;
//	}
//	
//	public void setUsersDao(IUsersDao usersDao) {
//		this.usersDao = usersDao;
//	}
}
