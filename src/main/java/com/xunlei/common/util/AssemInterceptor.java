/*
 * AssemInterceptor.java
 *
 * Created on 2007年8月28日, 下午4:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.util;

/**
 *  该接口主要提供在查询数据列表时需要对每个数据进行特殊处理的功能.
 *  该接口只在BaseDao 中的query方法中被使用到。
 *  
 * @see com.xunlei.common.dao.BaseDao
 *
 * @author 张金雄
 */
public interface AssemInterceptor<T> {
    public void hold(T obj);
}
