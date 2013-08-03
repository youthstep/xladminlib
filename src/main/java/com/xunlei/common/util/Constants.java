/*
 * Constants.java
 *
 * Created on 2007年8月24日, 上午10:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.util;

/**
 * 系统常量定义
 * @author 张金雄
 */
public final class Constants {
    
    /**
     * 类别类型，LibClassM表中的ClassNo字段值
     */
    public static final String SYSMODULE = "SysModule";//系统模块
    /**
     * 页面信息
     */
    public static final String PAGESIZE = "PageSize";//每页显示记录数 15
    /**
     * 树根的父节点ID值
     */
    public static final String TREEROOTPID = "-1";//树根的父节点ID值
    /**
     * 运行操作
     */    
    public static final String OPERATE_RUN = "run";
    /**
     * 查询操作
     */   
    //public static final String OPERATE_QUERY = "query";
    /**
     * 新增操作
     */   
    //public static final String OPERATE_ADD = "add";
    /**
     * 修改操作
     */   
    //public static final String OPERATE_EDIT = "edit";
    /**
     * 删除操作
     */   
    //public static final String OPERATE_DEL = "del";
    /**
     * 常用操作，包括运行、查询、新增、修改和删除
     */
    //public static final String[] COMMON_OPERATE = new String[]{OPERATE_RUN,OPERATE_QUERY,OPERATE_ADD,OPERATE_EDIT,OPERATE_DEL};
   // public static final String[] COMMON_OPERATE = new String[]{OPERATE_RUN,OPERATE_ADD,OPERATE_EDIT,OPERATE_DEL};
    /**
     * 系统角色
     */   
    public static final String ROLETYPE_SYS ="SYS";
    /**
     * 数据角色
     */   
    public static final String ROLETYPE_REC ="REC";
    /**
     * cookie存活时间数组
     */   
    public static final int[] COOKIE_TIME = { -1, 24 * 60 * 60, 7 * 24 * 60 * 60 ,365*24*60*60 };

    public static final String[] EXCLUDE_USERS=new String[]{"admin","administrator","root","super","superman","test","user"};//不可以注册的用户

    public static final String LOGINUSERCONTAINER="LoginUserContainer";
    public static final String LOGINCLIENTIP="loginclientip";
}
