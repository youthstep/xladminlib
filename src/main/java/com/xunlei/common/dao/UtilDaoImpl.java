package com.xunlei.common.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xunlei.common.event.XLRuntimeException;

@Component("utilDao")
public class UtilDaoImpl extends JdbcBaseDao implements IUtilDao {
    
    public UtilDaoImpl(){
        super();
    }
    /**
     * 根据对象名和where条件查询满足条件的记录数，where条件中引用类的别名为cls
     * @param cls 实体类型
     * @param where sql语句中where后面的内容
     * @return 满足条件的记录数
     */
    public int getRecordCount(Class cls,String where){
        String sql = "select count(*) from " + cls.getName().toLowerCase() + " as cls " ;
        if (isNotEmpty(where)){
            sql += " where " + where;
        }
        //return ((Integer)getHibernateTemplate().find(sql).get(0)).intValue();
        return this.getSingleInt(sql);
    }
    /**
     * 根据表名和where条件查询满足条件的记录数，where条件中引用表的别名为cls
     * @param cls 表名
     * @param where sql语句中where后面的内容
     * @return 满足条件的记录数
     */
    public int getRecordCount(String cls,String where){
        String sql = "select count(*) from " + cls.toLowerCase() + " as cls " ;
        if (isNotEmpty(where)){
            sql += " where " + where;
        }
        //return ((Integer)getHibernateTemplate().find(sql).get(0)).intValue();
        return this.getSingleInt(sql);
    }
    
	/**
	 * 执行更新的sql语句，返回更新成功的记录数
	 * @see com.xunlei.common.facade.FacadeCommonImpl#execUpdSql(String)
	 * 与JdbcBaseDao中的executeUpdate(String sql)功能相同
	 */
    public int execUpdSql(String sql) {
       return getJdbcTemplate().update(sql);
    }

    /**
     * 获得对应对象的记录。此方法委托给findObjects(T,String,String)处理。
     * @param <T> 实体类型
     * @param clazz 实体类型
     * @param where 查询条件
     * @return 实体类型的列表
     */
    public <T> List<T> getRecords(Class<T> clazz, String where) {
        try{
            T o=clazz.newInstance();
            return this.findObjects(o, where, null);
        }
        catch(Exception ex){
            throw new XLRuntimeException(ex);
        }
    }
    
}
