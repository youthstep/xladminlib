/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xunlei.common.dao;

import java.util.List;

/**
 *
 * @author IceRao
 */
public interface IUtilDao extends ICommonDao{
	/**
     * 获得对应对象的记录。此方法委托给findObjects(T,String,String)处理。
     * @param <T> 实体类型
     * @param cls 实体类型
     * @param where 查询条件
     * @return 实体类型的列表
     */
    public <T> List<T> getRecords(Class<T> cls,String where);
    
    /**
	 * 执行更新的sql语句，返回更新成功的记录数
	 * @see com.xunlei.common.facade.FacadeCommonImpl#execUpdSql(String)
	 * 与JdbcBaseDao中的executeUpdate(String sql)功能相同
	 */
    public int execUpdSql(String sql) ;

    /**
     * 根据对象名和where条件查询满足条件的记录数，where条件中引用类的别名为cls
     * @param cls 实体类型
     * @param where sql语句中where后面的内容
     * @return 满足条件的记录数
     */
    int getRecordCount(Class cls, String where);

    /**
     * 根据表名和where条件查询满足条件的记录数，where条件中引用表的别名为cls
     * @param cls 表名
     * @param where sql语句中where后面的内容
     * @return 满足条件的记录数
     */
    public int getRecordCount(String cls,String where);
    /**
     * 得到记录数，出任何错误都返回-1
     * 过期，用 getSingleInt(String sql) 替代
     */
}
