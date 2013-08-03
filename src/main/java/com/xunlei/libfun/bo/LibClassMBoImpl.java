/*
 * Created on 2006-03-10
*/
package com.xunlei.libfun.bo;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.StringTools;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.web.bean.QueryInfo;
import com.xunlei.libfun.dao.ILibClassMDao;
import com.xunlei.libfun.vo.LibClassM;

/**
 * 系统数组模块的业务逻辑实现层
 * 
 * @author jason
 */
@Service("libclassmService")
@RemoteProxy(name="libclassmService")
public class LibClassMBoImpl extends DataAccessBo<LibClassM> implements ILibClassMBo {
    @Autowired
	private ILibClassMDao libClassMDao;
    @Autowired
    public ILibClassDBo libClassDBo;
    
    /*****************以下为内部方法**********/
	/**
	 * 获得符合查询条件的LibClassM对象个数
	 * @param libclass 查询的条件
	 * @return 结果个数
	 */
	public int countLibclassm(LibClassM libclass) {
		return getLibClassMDao().countLibclassm(libclass);
	}
	

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibClassM()
	 */
	public List<LibClassM> getAllLibClassM() {
		return getLibClassMDao().getAllLibClassM();
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassMById(Long id)
	 */
	public LibClassM getLibClassMById(Long id) {
        LibClassM lm=new LibClassM();
        lm.setSeqid(id);
        return getLibClassMDao().getALibClassM(lm);
	}

	/**
	 * 根据数组编号获得LibClassM对象列表
	 * @param classno 数组编号
	 * @return LibClassM对象列表
	 */
	@SuppressWarnings("unused")
	private List<LibClassM> getLibClassMByClassNo(String classno) {
		LibClassM lm=new LibClassM();
        lm.setClassno(classno);
        return getLibClassMDao().getLibClassM(lm);

	}

	@SuppressWarnings("unused")
	private List<LibClassM> getLibClassMByClassName(String classname) {
		LibClassM lm=new LibClassM();
        lm.setClassname(classname);
        return getLibClassMDao().getLibClassM(lm);
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassM(LibClassM)
	 */
	public void insertLibClassM(LibClassM data) {
		LibClassM searchobj = new LibClassM();	
		searchobj.setClassno(data.getClassno());
		
		if (StringTools.isNotEmpty(data.getClassno())) {
			if (this.getLibClassMDao().countLibclassm(searchobj) > 0) {
				throw new XLRuntimeException("已经存在相同编号的数组");
			}
		}
		if (StringTools.isNotEmpty(data.getClassname())) {
			searchobj.setClassno("");
			searchobj.setClassname(data.getClassname());
			
			if (this.getLibClassMDao().countLibclassm(searchobj) > 0) {
				throw new XLRuntimeException("已经存在同名的数组");
			}
		}
		getLibClassMDao().insertLibClassM(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassM(LibClassM)
	 */
	public void updateLibClassM(LibClassM data) {
		LibClassM searchobj = new LibClassM();	
		LibClassM searchobj2 = new LibClassM();
		
		searchobj.setSeqid(data.getSeqid());
		searchobj.setClassno(data.getClassno());
		
		searchobj2.setClassno(data.getClassno());
		
		if (StringTools.isNotEmpty(data.getClassno())) {
			if (this.getLibClassMDao().countLibclassm(searchobj) != this.getLibClassMDao().countLibclassm(searchobj2)) {
				throw new XLRuntimeException("已经存在相同编号的数组");
			}
		}
		if (StringTools.isNotEmpty(data.getClassname())) {
			searchobj.setClassno("");
			searchobj.setClassname(data.getClassname());
			
			searchobj2.setClassno("");
			searchobj2.setClassname(data.getClassname());
			if (this.getLibClassMDao().countLibclassm(searchobj) != this
					.getLibClassMDao().countLibclassm(searchobj2)) {
				throw new XLRuntimeException("已经存在同名的数组");
			}
		}
		getLibClassMDao().updateLibClassM(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}

	/**
	 * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(LibClassM)
	 */
	public void removeLibClassM(LibClassM data) {
		if (libClassDBo.getLibClassDByClassNo(data.getClassno())
				.size() > 0) {
			throw new XLRuntimeException("类别明细不为空");
		}
		getLibClassMDao().removeLibClassM(data);
		// 清空缓存
		LibClassM.clearLibClassM(data.getClassno());
	}
	
	
	public ILibClassMDao getLibClassMDao() {
		return this.libClassMDao;
	}
	
	public void setLibClassMDao(ILibClassMDao libClassMDao) {
		this.libClassMDao = libClassMDao;
	}


	@Override
	public DataAccessReturn<LibClassM> query(LibClassM data, QueryInfo pinfo) {
		// TODO Auto-generated method stub
		return super.query(data, pinfo);
	}


	@Override
	public DataAccessReturn<LibClassM> insert(LibClassM data) {
		// TODO Auto-generated method stub
		return super.insert(data);
	}


	@Override
	public DataAccessReturn<LibClassM> update(LibClassM data) {
		// TODO Auto-generated method stub
		return super.update(data);
	}


	@Override
	public DataAccessReturn<LibClassM> deleteSome(LibClassM[] objs) {
		// TODO Auto-generated method stub
		return super.deleteSome(objs);
	}
}
