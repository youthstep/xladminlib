/*
 * Created on 2006-03-10
 */
package com.xunlei.libfun.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.XLService;
import com.xunlei.common.dao.IUtilDao;
import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.DatetimeUtil;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.util.StringTools;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.dao.ILibConfigDao;
import com.xunlei.libfun.vo.LibConfig;

/**
 * 系统配置模块的业务逻辑实现层
 * 
 * @author jason
 */
@Service("libconfigService")
@RemoteProxy(name="libconfigService")
public class LibConfigBoImpl extends XLService implements ILibConfigBo {
    @Autowired
	private ILibConfigDao libConfigDao;
    @Autowired
    public IUtilDao utilDao;

	@RemoteMethod
	public DataAccessReturn<LibConfig> query(LibConfig data,QueryInfo pinfo){
		return utilDao.findPagedObjects(data, pinfo.getSqlCondition(), new PagedFliper(pinfo));
	}
    
	/**
	 * 新增操作后增加相应的缓存新增操作
	 * @author liheng
	 * 2011-9-20
	 */
    @RemoteMethod
	public DataAccessReturn<LibConfig> insert (LibConfig data){
		DataAccessReturn<LibConfig> rtn = new DataAccessReturn<LibConfig>();
		data.setEditby(currentUserLogo());
		data.setEdittime(DatetimeUtil.now());
		utilDao.insertObject(data);
		//更新缓存
		LibConfig.insertLibConfig(data);
		return rtn;
	}
	
	/**
	 * 更新操作后增加相应的缓存更新操作
	 * @author liheng
	 * 2011-9-20
	 */
	@RemoteMethod
	public DataAccessReturn<LibConfig> update(LibConfig data){
		data.setEditby(currentUserLogo());
		data.setEdittime(DatetimeUtil.now());
		DataAccessReturn<LibConfig> rtn = new DataAccessReturn<LibConfig>();
		utilDao.updateObject(data);
		//更新缓存
		LibConfig.updateLibConfig(data);
		return rtn;
	}
	
	/**
	 * 删除操作后增加相应的缓存删除操作
	 * @author liheng
	 * 2011-9-20
	 */
	@RemoteMethod
	public DataAccessReturn<LibConfig> deleteSome(LibConfig[] objs){
		DataAccessReturn<LibConfig> rtn = new DataAccessReturn<LibConfig>();
		for(LibConfig obj : objs){
			utilDao.deleteObject(obj);
			//更新缓存
			LibConfig.deleteLibConfig(obj);
		}
		return rtn;
	}
    
	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibConfig()
	 */
	@Override
	public List<LibConfig> getAllLibConfig() {
		return getLibConfigDao().getAllLibConfig();
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibConfig(LibConfig)
	 */
	@Override
	public void updateLibConfig(LibConfig data) {
		LibConfig searchobj = new LibConfig();
		searchobj.setConfigno(data.getConfigno());
		
		LibConfig searchobj2 = new LibConfig();
		searchobj2.setConfigno(data.getConfigno());
		searchobj2.setSeqid(data.getSeqid());
		if (StringTools.isNotEmpty(data.getConfigno())) {
			if (this.getLibConfigDao().countLibconfigs(searchobj) != this.getLibConfigDao().countLibconfigs(searchobj2)) {
				throw new XLRuntimeException("已经存在相同编号的记录");
			}
		}
		if (StringTools.isNotEmpty(data.getConfigname())) {
			searchobj.setConfigno("");
			searchobj.setConfigname(data.getConfigname());

			searchobj2.setConfigname(data.getConfigname());
			searchobj2.setSeqid(data.getSeqid());

			if (this.getLibConfigDao().countLibconfigs(searchobj) != this.getLibConfigDao().countLibconfigs(searchobj2)) {
				throw new XLRuntimeException("已经存在同名的记录");
			}
		}
		data.setEdittime(DatetimeUtil.now());
		getLibConfigDao().updateLibConfig(data);
		// 清空缓存
		LibConfig.clearLibconfig();
	}

    
    public ILibConfigDao getLibConfigDao() {
		return this.libConfigDao;
	}
    
	public void setLibConfigDao(ILibConfigDao libConfigDao) {
		this.libConfigDao = libConfigDao;
	}
}
