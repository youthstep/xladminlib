package com.xunlei.libfun.bo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.BaseBo;
import com.xunlei.common.dao.IUtilDao;
import com.xunlei.libfun.vo.Privilege;
import com.xunlei.libfun.vo.Role;
import com.xunlei.libfun.vo.RolePrivilege;
import com.xunlei.libfun.vo.UsersRole;

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

	@Override
	@RemoteMethod
	public void insertUsersrole(String username, String roleno) {
		utilDao.insertObject(new UsersRole(username, roleno));
	}

	@Override
	@RemoteMethod
	public void deleteUsersroleByUsername(String username, Role[] datas) {
		utilDao.deleteObjectByCondition(new UsersRole(), "username='" + username + "' and roleno in ('" + StringUtils.join(datas, "','") + "')");
	}

	@Override
	@RemoteMethod
	public List<Privilege> queryRoleprivilegeByRoleno(String roleno) {
		return utilDao.query(Privilege.class, "select p.* from privilege p,roleprivilege rp where rp.roleno='" + roleno + "' and rp.prvilegeid=p.seqid");
	}

	@Override
	@RemoteMethod
	public List<Privilege> queryExcludedRoleprivilegeByRoleno(String roleno) {
		return utilDao.query(Privilege.class, "select * from privilege where seqid not in(select seqid from roleprivilege where roleno='" + roleno + "')");
	}

	@Override
	@RemoteMethod
	public void insertRoleprivilegeByRoleno(String roleno, long privilegeid) {
		utilDao.insertObject(new RolePrivilege(roleno, privilegeid));
	}

	@Override
	@RemoteMethod
	public void deleteRoleprivilegeByRoleno(String roleno, Privilege[] datas) {
		utilDao.deleteObjectByCondition(new RolePrivilege(), "roleno='" + roleno + "' privilegeid in (" + StringUtils.join(datas, ",") + ")");
	}
	
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
}
