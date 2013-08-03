/*
 * Created on 2006-03-10
 */
package com.xunlei.libfun.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.XLService;
import com.xunlei.common.dao.IUtilDao;
import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.DatetimeUtil;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.dao.ILibClassDDao;
import com.xunlei.libfun.vo.LibClassD;
import com.xunlei.libfun.vo.LibClassM;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 系统数组元素模块的业务逻辑实现层
 * 
 * @author jason
 */
@Service("libclassdService")
@RemoteProxy(name="libclassdService")
public class LibClassDBoImpl extends XLService implements ILibClassDBo {
    @Autowired
	private ILibClassDDao libClassDDao;
    @Autowired
    public IUtilDao utilDao;
    
	public DataAccessReturn<LibClassD> query(LibClassD data,QueryInfo pinfo){
		//by suepr 2012-12-5 15:58:25 修正分页
		return utilDao.findPagedObjects(data, pinfo.getSqlCondition(), new PagedFliper(pinfo));
	}
    
    /**
	 * @author liheng
	 * @since 2011-9-16
	 * 新增操作后在缓存中新增相应的系统数组元素
	 */
	public DataAccessReturn<LibClassD> insert(LibClassD data){
		DataAccessReturn<LibClassD> rtn = new DataAccessReturn<LibClassD>();
		data.setClassitemid(data.getClassno() + data.getItemno());
		data.setEditby(currentUserLogo());
		data.setEdittime(DatetimeUtil.now());
		utilDao.insertObject(data);
		//更新相应的数组
		LibClassM.insertLibClassDVo(data);
		return rtn;
	}
	
	/**
	 * @author liheng
	 * @since2011-9-16
	 * 更新操作后在缓存中更新相应的系统数组元素
	 */
	public DataAccessReturn<LibClassD> update(LibClassD data){
		data.setEditby(currentUserLogo());
		data.setEdittime(DatetimeUtil.now());
		DataAccessReturn<LibClassD> rtn = new DataAccessReturn<LibClassD>();
		utilDao.updateObject(data);
		//更新相应的数组
		LibClassM.updateLibClassDVo(data);
		return rtn;
	}
	
	/**
	 * @author liheng
	 * @since 2011-9-16
	 * 删除操作后在缓存中删除相应的系统数组元素
	 */
	public DataAccessReturn<LibClassD> deleteSome(LibClassD[] datas){
    	DataAccessReturn<LibClassD> rtn = new DataAccessReturn<LibClassD>();
		//更新相应的数组
		for(LibClassD obj : datas){
			utilDao.deleteObject(obj);
			LibClassM.deleteLibClassDVo(obj);
		}
		return rtn;
	}
    
	/**
	 * 根据classno获得所有符合条件的LibClassD结果集。
	 * @param classno 系统数组编号
	 * @return LibClassD对象的List
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassDByClassNo(String)
	 */
	public List<LibClassD> getLibClassDByClassNo(String classno) {
		return getLibClassDDao().getLibClassDByClassNo(classno);
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassDById(Long)
	 */
	public LibClassD getLibClassDById(Long id) {
		LibClassD ld=new LibClassD();
        ld.setSeqid(id);
        return getLibClassDDao().getLibClassD(ld);
	}

	
	public int countLibclassd(LibClassD libclass) {
		return getLibClassDDao().countLibclassd(libclass);
	}

	

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassD(LibClassD)
	 */
	public void insertLibClassD(LibClassD data) {

		LibClassD searchobj = new LibClassD();
		searchobj.setClassno(data.getClassno());
		searchobj.setItemno(data.getItemno());
		
		if (getLibClassDDao().countLibclassd(searchobj) > 0) {
			throw new XLRuntimeException("已经存在相同编号的元素");
		}
		data.setClassitemid(data.getClassno() + data.getItemno());
		getLibClassDDao().insertLibClassD(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassD(LibClassD)
	 */
	public void updateLibClassD(LibClassD data) {
		LibClassD searchobj = new LibClassD();
		searchobj.setSeqid(data.getSeqid());
		searchobj.setClassno(data.getClassno());
		searchobj.setItemno(data.getItemno());
		
		LibClassD searchobj2 = new LibClassD();
		searchobj2.setClassno(data.getClassno());
		searchobj2.setItemno(data.getItemno());
		
		if (this.getLibClassDDao().countLibclassd(searchobj) != this.getLibClassDDao().countLibclassd(searchobj2)) {
			throw new XLRuntimeException("已经存在相同编号的元素");
		}
		data.setClassitemid(data.getClassno() + data.getItemno());
		getLibClassDDao().updateLibClassD(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(LibClassD)
	 */
	public void removeLibClassD(LibClassD data) {
		if (utilDao.getRecordCount("functions", "cls.moduleno = '" + data.getClassitemid() + "'") > 0) {
			throw new XLRuntimeException("此元素已被功能模块引用，不能删除");
		}
		getLibClassDDao().removeLibClassD(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibClassds(LibClassD,PagedFliper)
	 */
	public DataAccessReturn<LibClassD> queryLibClassds(LibClassD ld, PagedFliper fliper) {
		return getLibClassDDao().queryLibClassds(ld, fliper);
	}

	public List<LibClassD> queryLibClassdsByUserlogno(UserInfo userInfo) {
		return getLibClassDDao().queryLibClassdsByUserlogno(userInfo);
	}

	
	public ILibClassDDao getLibClassDDao() {
		return this.libClassDDao;
	}
   
	public void setLibClassDDao(ILibClassDDao libClassDDao) {
		this.libClassDDao = libClassDDao;
	}

}
