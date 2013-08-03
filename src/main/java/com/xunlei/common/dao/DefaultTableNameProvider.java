package com.xunlei.common.dao;

import com.xunlei.common.dao.annotation.Table;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.HashMap;

/**
 * User: IceRao
 * Date: 2009-11-7
 * Time: 16:04:29
 * Description:
 */
public class DefaultTableNameProvider implements ITableNameProvider {
    protected Class clazz;
    protected Map<Object,Object> map=new HashMap<Object,Object>();
    public void setDaoClass(Class clazz) {
        this.clazz=clazz;
    }

    public void setVariable(Map<Object, Object> map) {
        this.map=map;
    }

    public void setVariable(Object key,Object value){
        this.map.put(key,value);
    }

    public void removeVariable(Object key) {
        this.map.remove(key);
    }

    /**
     * 获得对应vo的表名
     * 默认算法是：
     * 首先判断有没有Table这个注解，有的话以此注解定义的名称作为表名
     * 如果上面不成立的话，以clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase()作为数据库表名
     * @return 数据库表名
     */
    public String getTableName() {
        Table annotaion= clazz.getClass().getAnnotation(Table.class);
        if(annotaion!=null){
            return annotaion.name();
        }
        return clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
    }

}
