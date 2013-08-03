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
import com.xunlei.libfun.vo.LibClassD;
import com.xunlei.libfun.vo.UserInfo;

import org.springframework.stereotype.Component;

/**
 * 系统数组元素模块的底层数据访问实现类
 * 
 * @author jason
 */
@Component
@SuppressWarnings("unchecked")
public class LibClassDDaoImpl extends JdbcBaseDao implements ILibClassDDao{

    public LibClassDDaoImpl(){
        super();
    }

    /**
     * 根据classno获得所有符合条件的LibClassD结果集。
     * @param classno 系统数组编号
     * @return LibClassD对象的List
     * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassDByClassNo(String)
     */
    public List<LibClassD> getLibClassDByClassNo(String classno){
        if(isNotEmpty(classno)){
            String sql="select * from libclassd ld where ld.classno = '" + StringTools.escapeSql(classno) + "' order by ld.itemorder";
            return this.query(LibClassD.class, sql);
        }
        else{
            return null;
        }
    }

    /**
     * 获取一个LibClassD
     */
    public LibClassD findLibClassD(final LibClassD data){
        return this.findObjectByCondition(data);
    }

    /**
     * 插入一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassD(LibClassD)
     */
    public void insertLibClassD(LibClassD data){
        this.saveObject(data);
    }

    /**
     * 更新一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassD(LibClassD)
     */
    public void updateLibClassD(LibClassD data){
        this.updateObject(data);
    }

    /**
     * 删除一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(LibClassD)
     */
    public void removeLibClassD(LibClassD data){
        this.deleteObject(data);
    }

    /**
     * 删除一个主键为seqid的LibClassD对象
     * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(long)
     */
    public void removeLibClassD(long seqid){
        deleteObject("libclassd", seqid);
    }

    /**
     * 获得符合查询条件的LibClassD对象个数
     * @param libclass 查询的条件
     * @return 结果个数
     */
    public int countLibclassd(LibClassD libclass){
        // TODO Auto-generated method stub
        StringBuilder countsql=new StringBuilder("select count(*) from libclassd ");
        StringBuilder where=new StringBuilder(" where 1=1 ");
        int rowcount=-1;
        if(null != libclass){
            if(isNotEmpty(libclass.getClassno())){
                where.append(" and classno='").append(StringTools.escapeSql(libclass.getClassno())).append("'");
            }
            if(isNotEmpty(libclass.getItemno())){
                where.append(" and itemno='").append(StringTools.escapeSql(libclass.getItemno())).append("'");
            }
            if(libclass.getSeqid() > 0){
                where.append(" and seqid=").append(libclass.getSeqid());
            }
            rowcount=getSingleInt(countsql.append(where).toString());
        }
        return rowcount;
    }

    /**
     * 获得所有符合查询条件的LibClassD对象
     * @param ld 查询的条件
     * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
     * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibClassds(LibClassD,PagedFliper)
     */
    public DataAccessReturn<LibClassD> queryLibClassds(LibClassD ld, PagedFliper fliper){
        String where="";
        if(ld != null){
            if(isNotEmpty(ld.getClassno())){
                where+=" where classno ='" + StringTools.escapeSql(ld.getClassno()) + "' ";
            }
        }
        String countsql=" select count(*) from libclassd " + where;
        int rowcount=super.getSingleInt(countsql);
        if(rowcount == 0){
            return DataAccessReturn.EMPTY;
        }
        String sql="select * from libclassd " + where;
        if(fliper != null){
            if(fliper.isNotEmptySortColumn()){
                sql+=" order by " + fliper.getSortColumn();
            }
            sql+=fliper.limitsql(rowcount);
        }
        return new DataAccessReturn(rowcount, query(LibClassD.class, sql));
    }

    /**
     * 获得某一用户的LibClassD对象列表
     * @param userInfo 某一用户信息
     * @return LibClassD对象的List
     */
    public List<LibClassD> queryLibClassdsByUserlogno(UserInfo userInfo){
        if(userInfo != null && this.isNotEmpty(userInfo.getUserlogno())){
            String sql="select distinct t1.* from libclassd t1 left join functions t2 on t1.classItemId = t2.moduleNo " + "left join rolerights t3 on t2.funcNo=t3.funcNo " + "where t3.ENABLERUN =1 and t3.roleNo in (select RoleNo from usertorole where UserLogNo='" + StringTools.escapeSql(userInfo.getUserlogno()) + "') order by t1.itemorder";
            return this.query(LibClassD.class, sql);
        }
        else{
            return null;
        }
    }

    /**
     * 获取一个LibClassD
     */
    public LibClassD getLibClassD(LibClassD libclassd){
        return this.findObjectByCondition(libclassd);
    }
}
