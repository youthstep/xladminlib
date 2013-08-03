/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.dao;

import java.util.List;
import com.xunlei.common.dao.JdbcBaseDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.util.StringTools;
import com.xunlei.libfun.vo.LibClassM;

import org.springframework.stereotype.Component;

/**
 * 系统数组模块的底层数据访问实现类
 * 
 * @author jason
 */
@Component
@SuppressWarnings("unchecked")
public class LibClassMDaoImpl extends JdbcBaseDao implements ILibClassMDao{

    public LibClassMDaoImpl(){
        super();
    }

    /**
     * 获得所有的LibClassM结果集
     * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibClassM()
     */
    public List<LibClassM> getAllLibClassM(){
//
//		String sql = "select lm from LibClassM as lm order by lm.classno";
//		return getHibernateTemplate().find(sql);
        return this.findObjects(new LibClassM(), null, "classno");
    }

//	/**
//	 * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassMById(Long)
//	 */
//	public LibClassM getLibClassMById(Long id) {
//
//		try {
//			return (LibClassM) getHibernateTemplate().load(LibClassM.class, id);
//		} catch (org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException e) {
//			return null;
//		}
//	}
//
//
//	public List<LibClassM> getLibClassMByClassNo(String classno) {
//
//		String sql = "select lm from LibClassM as lm where lm.classno = ?";
//		return getHibernateTemplate().find(sql, classno);
//	}
//
//	public List getLibClassMByClassName(String classname) {
//
//		String sql = "select lm from LibClassM as lm where lm.classname = ?";
//		return getHibernateTemplate().find(sql, classname);
//	}
    /**
     * 获得符合条件的首个LibClassM对象
     */
    public LibClassM getALibClassM(LibClassM libclassm){
        return this.findObjectByCondition(libclassm);
    }

    /**
     * 获得所有符合查询条件的LibClassM对象
     * @param libclassm 查询的条件
     * @return LibClassM对象的List
     */
    public List<LibClassM> getLibClassM(LibClassM libclassm){
        return this.findObjects(libclassm, null, null);
    }

    /**
     * 插入一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassM(LibClassM)
     */
    public void insertLibClassM(LibClassM data){

        //getHibernateTemplate().save(data);
        this.saveObject(data);
    }

    /**
     * 更新一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassM(LibClassM)
     */
    public void updateLibClassM(LibClassM data){

        //getHibernateTemplate().update(data);
        this.updateObject(data);
    }

    /**
     * 删除一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(LibClassM)
     */
    public void removeLibClassM(LibClassM data){

        //getHibernateTemplate().delete(data);
        this.deleteObject(data);
    }

    /**
     * 删除一个指定seqid的LibClassM对象
     * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(long)
     */
    public void removeLibClassM(long seqid){
        deleteObject("libclassm", seqid);
    }

    /**
     * 获得符合查询条件的LibClassM对象个数
     * @param data 查询的条件
     * @return 结果个数
     */
    public int countLibclassm(LibClassM data){
        int rowcount=-1;
        if(null != data){
            StringBuilder countsql=new StringBuilder("select count(*) from libclassm");
            StringBuilder where=new StringBuilder(" where 1=1 ");
            if(isNotEmpty(data.getClassno())){
                where.append(" and classno='").append(StringTools.escapeSql(data.getClassno())).append("'");
            }
            if(isNotEmpty(data.getClassname())){
                where.append(" and classname='").append(StringTools.escapeSql(data.getClassname())).append("'");
            }
            if(data.getSeqid() > 0){
                where.append(" and seqid=").append(data.getSeqid());
            }
            return this.getSingleInt(countsql.append(where).toString());
        }
        return rowcount;
    }

    /**
     * 获得所有符合查询条件的LibClassM对象
     * @param config 查询的条件
     * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
     * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibclassms(LibClassM,PagedFliper)
     */
    public DataAccessReturn<LibClassM> queryLibclassms(LibClassM config, PagedFliper fliper){
        StringBuilder where=new StringBuilder(" where 1=1 ");
        if(config != null){
            if(isNotEmpty(config.getClassno())){
                where.append(" and classno = '").append(StringTools.escapeSql(config.getClassno())).append("'");
            }
            if(isNotEmpty(config.getClassname())){
                where.append(" and classname like '%" + StringTools.escapeSql(config.getClassname()) + "%' ");
            }
        }
        String countsql=" select count(*) from libclassm ";
        countsql+=where.toString();
        int rowcount=super.getSingleInt(countsql);
        if(rowcount == 0){
            return DataAccessReturn.EMPTY;
        }
        String sql="select * from libclassm ";
        sql+=where.toString();
        if(fliper != null){
            if(fliper.isNotEmptySortColumn()){
                sql+=" order by " + fliper.getSortColumn();
            }
            sql+=fliper.limitsql(rowcount);
        }
        return new DataAccessReturn(rowcount, query(LibClassM.class, sql));
    }
}
