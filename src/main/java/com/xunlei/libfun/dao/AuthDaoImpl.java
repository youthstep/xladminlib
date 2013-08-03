package com.xunlei.libfun.dao;

import org.springframework.stereotype.Component;

import com.xunlei.common.dao.BaseDao;

@Component
public class AuthDaoImpl extends BaseDao implements IAuthDao {

//	@Override
//	public List<Permission> queryPermissionsByRoleNo(String roleNo) {
//		return this.query(
//				Permission.class, 
//				"SELECT p.seqid,p.funname,p.svrname,p.name,p.remark FROM permission p , roletopermission rtp WHERE  p.seqid=rtp.permissionid and rtp.roleno='" + roleNo + "' order by svrname asc,funname asc");
//	}
//
//	@Override
//	public RoleToPermission insertRoleToPermission(RoleToPermission roleToPermission) {
//		return this.saveObject(roleToPermission);
//	}
//
//	@Override
//	public List<Permission> queryAllPermission() {
//		return query(Permission.class, "select * from permission order by svrname asc,funname asc");
//	}
//
//	@Override
//	public void deletePermissionById(long id) {
//		this.execute("delete from permission where seqid="+id);
//	}
//
//	@Override
//	public void insertPermission(Permission data) {
//		this.saveObject(data);
//	}
//
//	@Override
//	public void deleteRoleToPermission(RoleToPermission data) {
//		this.deleteObject(data);
//	}
//
//	@Override
//	public boolean hasPermission(String userlogo, String className,
//			String methodName) {
//		if(this.getSingleInt(
//				"SELECT COUNT(*) FROM permission WHERE svrname='"
//				+className
//				+"' AND funname='"+methodName
//				+"' AND seqid IN (SELECT permissionid FROM roletopermission WHERE roleno IN (SELECT roleno FROM usertorole WHERE userlogno='"
//				+userlogo+"'));")>0){
//			return true;
//		}else{
//			return false;
//		}
//		
//	}
//
//	@Override
//	public List<Users> getUsersByRoleno(String roleno) {
//		return this.query(
//				Users.class, 
//				"select * from users where userlogno in (select userlogno from usertorole where roleno='"+roleno+"')");
//	}
//
//	@Override
//	public List<Role> getRolesByUserlogno(String userlogno) {
//    	return this.query(
//    			Role.class, 
//    			"select r.* from roles r where  r.roleno in (select ur.roleno from usertorole ur where userlogno='"+userlogno+"')");
//	}
//
//	@Override
//	public List<Role> getRolesByPermissionid(long pid) {
//		return this.query(Role.class, 
//				"select * from roles where roleno in (select roleno from roletopermission where permissionid="+pid+")");
//	}
//
//	@Override
//	public UserToRole insertUserToRole(UserToRole data) {
//		return this.saveObject(data);
//	}
//
//	@Override
//	public void deleteUserToRole(UserToRole data) {
//		this.deleteObject(data);
//	}
//
//	@Override
//	public void deleteUserToRoleByUserlognoAndRoleno(String userlogno,
//			String roleno) {
//		this.execute("delete from usertorole where userlogno='" + userlogno + "' and roleno='" + roleno + "'");
//	}
//
//	@Override
//	public void deleteRoleToPermissionByRolenoAndPermissionid(String roleno,
//			String permissionid) {
//		this.execute("delete from roletopermission where roleno='" + roleno + "' and permissionid=" + permissionid);
//	}
//
//	@Override
//	public void deleteRoleToPermissionByPermissionId(long permissionid) {
//		this.execute("delete from roletopermission where permissionid=" + permissionid);
//	}
}
