package com.xunlei.libfun.bo;

import java.util.List;

import com.xunlei.libfun.vo.Privilege;


public interface IAuthBo {
	/**
	 * @param username
	 * @param type @see {@link com.xunlei.libfun.constant.PrivilegeTypes}
	 * @return
	 */
	List<Privilege> getUserPrivilegeByType(String username, int type);
	
//	/**
//	 * @param username
//	 * @param type
//	 * @param privlegeValue
//	 * @return
//	 */
//	boolean hasPrivlege(String username, int type, String privlegeValue);
	
	
	
	/**
	 * 查找所有spring内定义的@Service标签的bo和bo内标注@RemoteMethod的方法，如果数据库不存在则插入数据库，如果数据库存在但代码里面不存在则删除
	 */
//	void refreshServicePermission();
//	
//	boolean hasPermission(String userlogo,String className,String methodName);
//	
//	List<Users> getUsersByRoleno(String roleno);
//	
//	List<Role> getRolesByUserlogno(String userlogno);
//	
//	UserToRole insertUserToRole(UserToRole data);
//	
//	void deleteUserToRole(UserToRole data);
//	
//	void deleteUserToRoleByUserlognoAndRoleno(String userlogno,String roleno);
//	
//	List<Role> getAllRoles();
//	
//	List<Role> getRolesByPermissionid(long pid);
//	
//	/**
//	 * 得到所有权限，使用roleno过滤
//	 * @param roleno
//	 * @return
//	 */
//	List<Permission> getPermissionsByRoleno(String roleno);
//	
//	RoleToPermission insertRoleToPermission(RoleToPermission roleToPermission);
//	
//	void deleteRoleToPermission(RoleToPermission data);
//	
//	void deleteRoleToPermissionByRolenoAndPermissionid(String roleno,String permissionid);
//	
//	List<Permission> getAllPermission();
}
