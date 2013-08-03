/*
 * WebContext.java
 *
 * Created on 2006年11月3日, 下午3:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.web.model;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xunlei.common.util.ApplicationConfigUtil;
import com.xunlei.common.util.Constants;
import com.xunlei.common.web.WebApplicationContextUtil;

/**
 *
 * @author kamhung
 */
public class WebContext implements ServletContextListener {
    
    public static String WEBROOT ="/";
    
    private String tempdir = "";
    
    /**
     *初始化数据库源，设置临时文件夹
     */
    public void contextInitialized(ServletContextEvent sce){
        WEBROOT =sce.getServletContext().getRealPath("").replace('\\', '/') + '/';
        tempdir = WEBROOT + "tempdir";
        File f=new File(tempdir);
        if(!f.exists()){
            f.mkdirs();
        }
        System.out.println("TEMPDIR = "+tempdir);
        System.setProperty("java.io.tmpdir", tempdir);
        System.out.println("WEBROOT = " + WEBROOT);
        //从配置文件路径中获取相应的spring bean
        WebApplicationContextUtil.webApplicationContext  = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    } 
    
    /**
     *退出时清除临时文件
     */
    public void contextDestroyed(ServletContextEvent sce){
        sce.getServletContext().removeAttribute(Constants.LOGINUSERCONTAINER);
        if(!ApplicationConfigUtil.isClearupTempdir()) return;
        File root = new File(tempdir);
        if(!root.isDirectory()) return;
        for(File f : root.listFiles()) {
            f.delete();
        }
    }
    
}
