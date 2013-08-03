/*
 * Created on 2006-03-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.libfun.dao;

import com.xunlei.common.dao.JdbcBaseDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.common.util.StringTools;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.xunlei.libfun.vo.LibConfig;

import org.springframework.stereotype.Component;

/**
 * 系统配置模块的底层数据访问实现类
 * 
 * @author jason
 * 
 */
@Component
@SuppressWarnings("unchecked")
public class LibConfigDaoImpl extends JdbcBaseDao implements ILibConfigDao{

    public LibConfigDaoImpl(){
        super();
    }

    /**
     * 获得所有的LibConfig结果集
     * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibConfig()
     */
    public List<LibConfig> getAllLibConfig(){
        return this.findObjects(new LibConfig(), null, null);
    }

    /**
     * 插入一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibConfig(LibConfig)
     */
    public void insertLibConfig(LibConfig data){
        this.saveObject(data);
    }

    /**
     * 更新一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibConfig(LibConfig)
     */
    public void updateLibConfig(LibConfig data){
        this.updateObject(data);
    }

    /**
     * 删除一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibConfig(LibConfig)
     */
    public void removeLibConfig(LibConfig data){
        this.deleteObject(data);
    }

    /**
     * 删除一个主键为seqid的LibConfig对象
     * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibConfig(long)
     */
    public void removeLibConfig(long seqid){
        deleteObject("libconfig", seqid);
    }

    /**
     * 获得所有符合查询条件的LibConfig对象
     * @param config 查询的条件
     * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
     * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibconfigs(LibConfig,PagedFliper)
     */
    public DataAccessReturn<LibConfig> queryLibconfigs(LibConfig config, PagedFliper fliper){
        StringBuilder where=new StringBuilder(" where 1=1 ");
        if(config != null){
            if(isNotEmpty(config.getConfigno())){
                where.append(" and configno = '").append(StringTools.escapeSql(config.getConfigno())).append("'");
            }
            if(isNotEmpty(config.getConfigname())){
                where.append(" and configname like '%" + StringTools.escapeSql(config.getConfigname()) + "%' ");
            }
        }
        String countsql=" select count(*) from libconfig ";
        countsql+=where.toString();
        int rowcount=super.getSingleInt(countsql);
        if(rowcount == 0){
            return DataAccessReturn.EMPTY;
        }
        String sql="select * from libconfig ";
        sql+=where.toString();
        if(fliper != null){
            if(fliper.isNotEmptySortColumn()){
                sql+=" order by " + fliper.getSortColumn();
            }
            sql+=fliper.limitsql(rowcount);
        }
        return new DataAccessReturn(rowcount, query(LibConfig.class, sql));
    }

    /**
     * 获得符合查询条件的LibConfig对象个数
     * @param config 查询的条件
     * @return 结果个数
     */
    public int countLibconfigs(LibConfig config){
        int rowcount=-1;
        if(config != null){
            StringBuilder countsql=new StringBuilder("select count(1) from libconfig ");
            StringBuilder where=new StringBuilder(" where 1=1 ");
            if(isNotEmpty(config.getConfigno())){
                where.append(" and configno='").append(StringTools.escapeSql(config.getConfigno())).append("'");
            }
            if(config.getSeqid() > 0){
                where.append(" and seqid=").append(config.getSeqid());
            }
            if(isNotEmpty(config.getConfigname())){
                where.append(" and configname='").append(StringTools.escapeSql(config.getConfigname())).append("'");
            }
            rowcount=this.getSingleInt(countsql.append(where).toString());
        }
        return rowcount;
    }

    /**
     * 获得符合条件的首个LibConfig对象
     */
    public LibConfig getALibConfig(LibConfig libconfig){
        return this.findObjectByCondition(libconfig);
    }

    /**
     * 获得符合条件的LibConfig结果集
     */
    public List<LibConfig> getLibConfig(LibConfig libconfig){
        return this.findObjects(libconfig, null, null);
    }
}
