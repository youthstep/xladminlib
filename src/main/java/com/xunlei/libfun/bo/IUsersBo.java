package com.xunlei.libfun.bo;

import com.xunlei.libfun.vo.UserInfo;
import com.xunlei.libfun.vo.Users;

/**
 * 系统用户模块的业务接口
 * 
 * @author Brice Li
 */
public interface IUsersBo {
	public UserInfo login(String userlogo,String password,String ip);
	
	public Users getUsersByUserLogNo(String userlogno);
//    /**
//     * 重置某用户密码
//     * @param seqid
//     * @param password
//     */
//	public void resetPassword(long seqid,String password);
//	public List<BaseCopartners> queryCopartners();
//	public List<Users> queryUpusers();
//	public List<Roles> queryRolesRelateUser(Users user);
//	public List<Roles> queryRolesNotRelateUser(Users user);
//	public DataAccessReturn<Roles> addRolesToUser(long[] sids,Users user);
//	public DataAccessReturn<Roles> removeRolesToUser(long[] sids,Users user);
//	public void updateSelfInfo(String oldPwd,String newPwd,String tel,String qq,String mail);
//	public Users querySelfInfo();
//	public List<String> getAllUserLogo();
//	public Map<String, String> getCopartnerNoAndName();
//	public int getCountUsers(Users data);
//	public boolean isSuperMan(Users u);
//	public Users findUsers(Users data);
//	public Users findAUsers(Users users);
//	
//	/**
//	 * 根据用户帐号获得用户对象
//	 * @param userlogno 用户帐号
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getUsersByUserLogNo(String)
//	 */
//	public Users getUsersByUserLogNo(String userlogno);
//
//	/**
//	 * @return 返回在用的业务用户（只包含属性CopartnerNo, CopartnerName）的列表。
//	 * 
//	 */
//	public List<BaseCopartners> getBaseCopartnerNoAndName();
//	
//	/**
//	 * 是否对用户进行数据控制
//	 * @param userlogno 用户帐号
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getIfMyDataOnly(String)
//	 */
//	public boolean getIfMyDataOnly(String userlogno);
//
//    /**
//	 * 获得所有的上级主帐号
//	 */
//	public Map<String, String> getUpuserlognolist();
//    /**
//	 * 获得所有的上级主帐号(users列表)
//	 */
//	public List<Users> getUpuserslist();
//	
//    /**
//     * 判断某帐号是否是某帐号的子帐号
//     * @param subaccount 子帐号
//     * @param fatheraccount 主帐号
//     * @return 当帐号是某帐号的子帐号时返回true，否则返回false
//     */
//    public boolean isSubAccountof(String subaccount,String fatheraccount);
//
//    /**
//     * 获得前台用户的用户名和真实名类表，每项使用|进行分割
//     * @return
//     */
//    public String[] getFrontUsersLognoAndTrueName();
//
//    /**
//     * 查询当前用户登录相关的信息
//     * @param onlineUserInfoShow
//     * @param fliper
//     * @return
//     */
//    public DataAccessReturn<OnlineUserInfoShow> queryAllUsersWithOnlineInfo(OnlineUserInfoShow onlineUserInfoShow,PagedFliper fliper);
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
//    public void updatePassword(String userlogno,String oldPassword,String newPassword,String tel,String qq,String mail);
//
//    /**
//     * 修改用户信息逻辑
//     * @param user
//     */
//    public void editUsers(Users user);
//
//     /**
//     * 登陆操作，用户成功登陆后返回的userinfo包含该用户的角色信息以及其他的基本信息。
//     * @param userlogo
//     */
//    public UserInfo Login(String userlogo,String password,String ip);
}
