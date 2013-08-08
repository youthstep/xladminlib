package com.xunlei.libfun.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.BaseBo;
import com.xunlei.common.dao.IUtilDao;
import com.xunlei.libfun.vo.Privilege;
import com.xunlei.libfun.vo.Role;

@Service("authService")
@RemoteProxy(name="authService")
public class AuthBoImpl extends BaseBo implements IAuthBo{
	@Autowired
	public IUtilDao utilDao;
	
	@Override
	public List<Privilege> getUserPrivilegeByType(String username, int type) {
		return utilDao.query(
				Privilege.class
				, "select p.* from usersrole ur,roleprivilege rp,privilege p where ur.username='" + username +"' and ur.roleno=rp.roleno and rp.privilegeid=p.seqid and p.type=" + type);
	}

	@Override
	@RemoteMethod
	public List<Role> queryRoleByUsername(String username) {
		return utilDao.query(Role.class, "select r.* from role r,usersrole ur where ur.username='" + username + "' and ur.roleno=r.no");
	}

	@Override
	@RemoteMethod
	public List<Role> queryExcludedRoleByUsername(String username) {
		return utilDao.query(Role.class, "select * from role where no not in(select roleno from usersrole where username='" + username + "')");
	}
	
//	@Override
//	public boolean hasPrivlege(String username, int type, String privlegeValue) {
//		return false;
//	}
	
//	@Autowired
//	public IRolesDao rolesDao;
//
//	@Override
//	@RemoteMethod
//	public void refreshServicePermission() {
//		List<Permission> oldList = authDao.queryAllPermission();
//		Map<String,Permission> oldMap = new HashMap<String, Permission>();
//		for(Permission p : oldList){
//			//name不为“ ”是自定义权限，不在刷新的范围
//			if(StringTools.isEmpty(p.getName())){
//				oldMap.put(p.getSvrname()+p.getFunname(), p);
//			}
//		}
//		Map<String, Object> beansMap = WebApplicationContextUtil.webApplicationContext.getBeansWithAnnotation(RemoteProxy.class);
//		Iterator<Map.Entry<String, Object>> iter = beansMap.entrySet().iterator();
//		while (iter.hasNext()) { 
//		    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next(); 
//		    Object bean = entry.getValue();
//		    Class<? extends Object> beanClass = bean.getClass();
//		    Method[] methods = beanClass.getMethods();
//		    for(Method method : methods){
//		    	if(method.getAnnotation(RemoteMethod.class)!=null){
//		    		String key=beanClass.getName()+method.getName();
//		    		if(oldMap.get(key)==null){
//		    			Permission permission = new Permission();
//			    		permission.setSvrname(beanClass.getName());
//			    		permission.setFunname(method.getName());
//			    		permission.setName("自动生成");
//			    		authDao.insertPermission(permission);
//		    		}else{
//		    			oldMap.remove(key);
//		    		}
//		    	}
//		    }
//		}
//		//oldMap剩下的都是应该删除的权限
//		Iterator<Map.Entry<String, Permission>> it = oldMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String, Permission> entry = (Map.Entry<String, Permission>) it.next(); 
//			long pid = entry.getValue().getSeqid();
//			authDao.deletePermissionById(pid);
//			authDao.deleteRoleToPermissionByPermissionId(pid);
//		}
//	}
//	
//	@Override
//	@RemoteMethod
//	public List<Permission> getPermissionsByRoleno(String roleno) {
//		return authDao.queryPermissionsByRoleNo(roleno);
//	}
//	
//	@Override
//	public boolean hasPermission(String userlogo, String className,
//			String methodName) {
//		return authDao.hasPermission(userlogo, className, methodName);
//	}
//
//	@Override
//	@RemoteMethod
//	public List<Role> getAllRoles() {
//		return rolesDao.getAllRoles();
//	}
//
//	@Override
//	@RemoteMethod
//	public RoleToPermission insertRoleToPermission(RoleToPermission roleToPermission) {
//		return authDao.insertRoleToPermission(roleToPermission);
//	}
//
//	@Override
//	@RemoteMethod
//	public List<Permission> getAllPermission() {
//		return authDao.queryAllPermission();
//	}
//
//	@Override
//	@RemoteMethod
//	public List<Users> getUsersByRoleno(String roleno) {
//		return authDao.getUsersByRoleno(roleno);
//	}
//
//	@Override
//	@RemoteMethod
//	public List<Role> getRolesByUserlogno(String userlogno) {
//		return authDao.getRolesByUserlogno(userlogno);
//	}
//
//	@Override
//	@RemoteMethod
//	public List<Role> getRolesByPermissionid(long pid) {
//		return authDao.getRolesByPermissionid(pid);
//	}
//
//	@Override
//	@RemoteMethod
//	public UserToRole insertUserToRole(UserToRole data) {
//		return authDao.insertUserToRole(data);
//	}
//
//	@Override
//	@RemoteMethod
//	public void deleteUserToRole(UserToRole data) {
//		authDao.deleteUserToRole(data);
//	}
//
//	@Override
//	@RemoteMethod
//	public void deleteRoleToPermission(RoleToPermission data) {
//		authDao.deleteRoleToPermission(data);
//	}
//
//	@Override
//	public void deleteUserToRoleByUserlognoAndRoleno(String userlogno,
//			String roleno) {
//		authDao.deleteUserToRoleByUserlognoAndRoleno(userlogno, roleno);
//	}
//
//	@Override
//	public void deleteRoleToPermissionByRolenoAndPermissionid(String roleno,
//			String permissionid) {
//		authDao.deleteRoleToPermissionByRolenoAndPermissionid(roleno, permissionid);
//	}
}
