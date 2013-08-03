package com.xunlei.common.dao;

import java.util.Map;

/**
 * User: IceRao
 * Date: 2009-11-7
 * Time: 15:57:52
 * Description: 此接口用于提供底层dao生成数据库sql操作语句时的库名和表名生成。
 */
public interface ITableNameProvider {
    /**
     * 设置dao的class
     * @param clazz
     */
    void setDaoClass(Class clazz);

    /**
     * 设置用于库名，表名运算的参数
     * @param map
     */
    void setVariable(Map<Object,Object> map);

    /**
     * 用于添加参数及其值
     * @param key
     * @param value
     */
    public void setVariable(Object key,Object value);

    /**
     * 用于移除参数
     * @param key
     */
    public void removeVariable(Object key);

    /**
     * 获得表名
     * @return
     */
    String getTableName();

}
