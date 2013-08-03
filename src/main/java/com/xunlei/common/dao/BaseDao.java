package com.xunlei.common.dao;


/**
 * 继承自Jdbc操作的积累，但同时提供对Hibernate的支持。此类主要处理hibernate的sessionFactory的注入（并重写BaseDao中关于数据持久化的方法）
 * 创建时间：2008-10-24 17:29:27
 * @author IceRao
 * @author Brice Li(tcwdsg@gmail.com)
 */
public abstract class  BaseDao extends JdbcBaseDao {

}
